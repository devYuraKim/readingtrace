package com.yurakim.readingtrace.book.controller;

import com.yurakim.readingtrace.book.dto.BookDto;
import com.yurakim.readingtrace.book.dto.GoogleBooksSearchResultDto;
import com.yurakim.readingtrace.book.service.BookService;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping(ApiPath.BOOK) // /api/v1/books
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<GoogleBooksSearchResultDto> searchBook(@RequestParam String searchType, @RequestParam String searchWord, @RequestParam int startIndex, @RequestParam(name = "maxResults") int booksPerPage){
        return ResponseEntity.ok(bookService.searchBook(searchType, searchWord, startIndex, booksPerPage));
    }

    @GetMapping(value = "reactive/searchBook", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<BookDto>> reactiveSearchBook(@RequestParam String searchType, @RequestParam String searchWord) {
        System.out.println("Initial auth: " + SecurityContextHolder.getContext().getAuthentication());

        return Flux.defer(() ->
                        ReactiveSecurityContextHolder.getContext()
                                .cast(SecurityContext.class)
                                .flatMapMany(securityContext -> {
                                    System.out.println("Reactive auth: " + securityContext.getAuthentication());

                                    return bookService.reactiveSearchBook(searchType, searchWord)
                                            .delayElements(Duration.ofMillis(500)) // optional: simulate streaming
                                            .map(book -> ServerSentEvent.<BookDto>builder()
                                                    .data(book)
                                                    .build())
                                            .doOnNext(sse -> System.out.println("Emitting: " + sse.data().getTitle()))
                                            .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
                                })
                                .switchIfEmpty(
                                        Mono.fromCallable(SecurityContextHolder::getContext)
                                                .cast(SecurityContext.class)
                                                .flatMapMany(securityContext -> {
                                                    System.out.println("Fallback auth: " + securityContext.getAuthentication());

                                                    return bookService.reactiveSearchBook(searchType, searchWord)
                                                            .delayElements(Duration.ofMillis(500))
                                                            .map(book -> ServerSentEvent.<BookDto>builder()
                                                                    .data(book)
                                                                    .build())
                                                            .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
                                                })
                                )
                )
                .doOnComplete(() -> System.out.println("Stream completed"))
                .doOnError(error -> System.out.println("Stream error: " + error.getMessage()));
    }

}
