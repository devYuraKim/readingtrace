package com.yurakim.readingtrace.book.service.serviceImpl;

import com.yurakim.readingtrace.book.dto.BookSearchResultDto;
import com.yurakim.readingtrace.book.dto.GoogleBookDto;
import com.yurakim.readingtrace.book.dto.GoogleBooksResponseDto;
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
    public BookSearchResultDto searchBook(String searchType, String searchWord, int startIndex, int booksPerPage) {
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
        GoogleBooksResponseDto response = client
                .get()
                .uri(url)
                .retrieve()
                .body(GoogleBooksResponseDto.class);

        if(response.getItems() == null){
            return new BookSearchResultDto(Collections.emptyList(), 0);
        }

        List<GoogleBookDto> books =  response.getItems().stream()
            .map(this::mapToGoogleBookDto)
            .collect(Collectors.toList());

        return new BookSearchResultDto(books, response.getTotalItems());
    }

    @Override
    public Flux<GoogleBookDto> reactiveSearchBook(String searchType, String searchWord) {
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
                .bodyToMono(GoogleBooksResponseDto.class)
                .timeout(Duration.ofSeconds(30))
                .flatMapMany(response -> {
                    if (response.getItems() == null || response.getItems().isEmpty()) {
                        return Flux.empty();
                    }

                    // Convert to Flux and emit one by one
                    return Flux.fromIterable(response.getItems())
                            .map(this::mapToGoogleBookDto)
                            .doOnNext(book -> System.out.println("**********Service emitting: " + book.getTitle()));
                })
                .onErrorResume(Exception.class, ex -> {
                    System.err.println("**********Error in searchBooks: " + ex.getMessage());
                    ex.printStackTrace();
                    return Flux.empty();
                });

    }

    private GoogleBookDto mapToGoogleBookDto(GoogleBooksResponseDto.BookItem item){
        GoogleBooksResponseDto.VolumeInfo vi = item.getVolumeInfo();

        List<String> authors = vi.getAuthors() != null ? vi.getAuthors() : List.of();
        List<GoogleBooksResponseDto.IndustryIdentifier> ids =
                vi.getIndustryIdentifiers() != null ? vi.getIndustryIdentifiers() : List.of();

        return new GoogleBookDto(
                item.getId(),
                vi.getTitle(),
                String.join(", ", authors),
                vi.getImageLinks() != null ? vi.getImageLinks().getThumbnail() : "",
                vi.getPublisher(),
                vi.getPublishedDate(),
                vi.getDescription(),
                ids.size() > 0 ? ids.get(0).getIdentifier() : "",
                ids.size() > 1 ? ids.get(1).getIdentifier() : ""
        );
    }

}
