package com.yurakim.readingtrace.book.service.serviceImpl;

import com.yurakim.readingtrace.auth.security.UserDetailsImpl;
import com.yurakim.readingtrace.book.dto.BookDto;
import com.yurakim.readingtrace.book.dto.GoogleBookDto;
import com.yurakim.readingtrace.book.dto.GoogleBooksSearchResultDto;
import com.yurakim.readingtrace.book.entity.Book;
import com.yurakim.readingtrace.book.mapper.BookMapper;
import com.yurakim.readingtrace.book.repository.BookRepository;
import com.yurakim.readingtrace.book.repository.UserReadingStatusRepository;
import com.yurakim.readingtrace.book.service.BookService;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final WebClient webClient;
    private final Environment env;

    private final BookRepository bookRepository;
    private final UserReadingStatusRepository userReadingStatusRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto createOrGetBook(BookDto bookDto) {
        Book book = bookRepository
                .findByExternalId(bookDto.getExternalId())
                .orElseGet(() -> {
                    Book newBook = bookMapper.mapToEntity(bookDto);
                    return bookRepository.save(newBook);
                });
        return bookMapper.mapToDto(book);
    }

    @Override
    public BookDto getBookById(@PathVariable Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(()->new RuntimeException("No book found with id: " + bookId));
        return bookMapper.mapToDto(book);
    }

    @Override
    public GoogleBooksSearchResultDto searchBook(String searchType, String searchWord, int startIndex, int booksPerPage) {
        String trimmedSerchWord = searchWord.trim().replaceAll("\\s+", " ");
        String searchPrefix = switch (searchType) {
            case "author" -> "inauthor:";
            case "isbn" -> "isbn:";
            default -> "intitle:";
        };

        String url = ApiPath.GOOGLE_BOOK_BASE
                + "?key=" + env.getProperty("api.google.books.key")
                + "&printType=" + "books"
                + "&q=" + searchPrefix + trimmedSerchWord
                + "&maxResults=" + booksPerPage
                + "&startIndex=" + startIndex;

        RestClient client = RestClient.create();

        //searchWord will be encoded here, no need to do it before; causes double encoding issue
        GoogleBookDto response = client
                .get()
                .uri(url)
                .retrieve()
                .body(GoogleBookDto.class);

        if(response.getItems() == null){
            return new GoogleBooksSearchResultDto(Collections.emptyList(), 0);
        }

        //TODO: check if single query would be better than multiple steps (note for Oct 24)
        //1. when converting the Google API response to BookDto, collect a set of externalIds
        List<BookDto> bookDtoList = new ArrayList<>();
        Set<String> externalIds =  response.getItems().stream()
            .map(bookItem -> {
                BookDto bookDto = mapToBookDto(bookItem);
                bookDtoList.add(bookDto);
                return bookDto.getExternalId();
            })
            .collect(Collectors.toSet());

        //2. [conversion: externalId → bookId] use externalIds to query the Book table and retrieve a set of internal bookIds (indicating the books already exist in the DB)
        Set<Long> existingBookIds = bookRepository.findIdsIn(externalIds);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        //3. using the retrieved bookIds and the current userId, query UserReadingStatus to find matching entries (indicating the user has added those books)
        Set<Long> addedBookIds = userReadingStatusRepository.findByUserIdAndBookIdsIn(userId, existingBookIds);

        //4. [conversion: bookId → externalId] use the matching bookIds to look up their corresponding externalIds from the Book table
        Set<String> addedExternalIds = bookRepository.findExternalIdsIn(addedBookIds);

        //5. iterate through the BookDto list and, for each entry, set isAdded = true if its externalId is found in the above set
        bookDtoList.forEach(bookDto -> {
            boolean isAdded = addedExternalIds.contains(bookDto.getExternalId());
            bookDto.setIsAdded(isAdded);
        });

        return new GoogleBooksSearchResultDto(bookDtoList, response.getTotalItems());
    }

    @Override
    public Flux<BookDto> reactiveSearchBook(String searchType, String searchWord) {
        String trimmedSerchWord = searchWord.trim().replaceAll("\\s+", " ");
        String searchPrefix = switch (searchType) {
            case "author" -> "inauthor:";
            case "isbn" -> "isbn:";
            default -> "intitle:";
        };
        String rawQuery = searchPrefix + trimmedSerchWord;

        String url = ApiPath.GOOGLE_BOOK_BASE
                + "?key=" + env.getProperty("api.google.books.key")
                + "&printType=" + "books"
                + "&q=" + rawQuery;

        return webClient.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        Mono.error(new RuntimeException("**********Google Books API error: " + response.statusCode())))
                .bodyToMono(GoogleBookDto.class)
                .timeout(Duration.ofSeconds(30))
                .flatMapMany(response -> {
                    if (response.getItems() == null || response.getItems().isEmpty()) {
                        return Flux.empty();
                    }

                    // Convert to Flux and emit one by one
                    return Flux.fromIterable(response.getItems())
                            .map(this::mapToBookDto)
                            .doOnNext(book -> System.out.println("**********Service emitting: " + book.getTitle()));
                })
                .onErrorResume(Exception.class, ex -> {
                    System.err.println("**********Error in searchBooks: " + ex.getMessage());
                    ex.printStackTrace();
                    return Flux.empty();
                });

    }

    private BookDto mapToBookDto(GoogleBookDto.BookItem item){
        GoogleBookDto.VolumeInfo vi = item.getVolumeInfo();

        List<GoogleBookDto.IndustryIdentifier> ids =
                vi.getIndustryIdentifiers() != null ? vi.getIndustryIdentifiers() : List.of();

        return new BookDto(
                null, //bookId
                item.getId(), //externalId
                vi.getTitle(), //title
                vi.getAuthors(), //authors
                vi.getImageLinks() != null ? vi.getImageLinks().getThumbnail() : "", //imageLinks
                vi.getPublisher(), //publisher
                vi.getPublishedDate(), //publishedDate
                vi.getDescription(), //description
                ids.size() > 0 ? ids.get(0).getIdentifier() : "", //isbn10
                ids.size() > 1 ? ids.get(1).getIdentifier() : "", //isbn13
                vi.getPageCount(), //pageCount
                vi.getMainCategory(), // String mainCategory
                vi.getCategories(), //categories
                vi.getAverageRating(), //Double averageRatings
                vi.getRatingsCount(), //Long ratingsCount
                vi.getLanguage(), //String lanauge
                false
        );
    }

}
