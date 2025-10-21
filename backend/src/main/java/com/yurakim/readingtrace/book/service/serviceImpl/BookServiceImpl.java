package com.yurakim.readingtrace.book.service.serviceImpl;

import com.yurakim.readingtrace.book.dto.BookDto;
import com.yurakim.readingtrace.book.dto.GoogleBooksSearchResultDto;
import com.yurakim.readingtrace.book.dto.GoogleBookDto;
import com.yurakim.readingtrace.book.service.BookService;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final WebClient webClient;
    private final Environment env;

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

        List<BookDto> books =  response.getItems().stream()
            .map(this::mapToBookDto)
            .collect(Collectors.toList());

        return new GoogleBooksSearchResultDto(books, response.getTotalItems());
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
                vi.getLanguage() //String lanauge
        );
    }

    private Long bookId;
    private String externalId;
    private String title;
    private String authors;
    private String imageLinks;
    private String publisher;
    private String publishedDate;
    private String description;
    private String isbn10;
    private String isbn13;
    private Integer pageCount;
    private String mainCategory;
    private String categories;
    private Double averageRating;
    private Long ratingsCount;
    private String language;


}
