package com.yurakim.readingtrace.book.controller;

import com.yurakim.readingtrace.book.dto.GoogleBookDto;
import com.yurakim.readingtrace.book.dto.BookSearchResultDto;
import com.yurakim.readingtrace.book.dto.UserBookDto;
import com.yurakim.readingtrace.book.service.BookService;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping(ApiPath.BOOK) // /api/v1/books
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<BookSearchResultDto> searchBook(@RequestParam String searchType, @RequestParam String searchWord, @RequestParam int startIndex, @RequestParam(name = "maxResults") int booksPerPage){
        return ResponseEntity.ok(bookService.searchBook(searchType, searchWord, startIndex, booksPerPage));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUserBook(@RequestBody UserBookDto userBookDto) {
        bookService.addUserBook(userBookDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserBookDto>> getUserBooks(
            @PathVariable Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String visibility,
            @RequestParam(required = false) Integer rating
    ) {
        List<UserBookDto> userBookList = bookService.getUserBooks(userId, status, visibility, rating);
        return ResponseEntity.ok(userBookList);
    }

    @GetMapping(value = "reactive/searchBook", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<GoogleBookDto>> reactiveSearchBook(@RequestParam String searchType, @RequestParam String searchWord) {
        System.out.println("Initial auth: " + SecurityContextHolder.getContext().getAuthentication());

        return Flux.defer(() ->
                        ReactiveSecurityContextHolder.getContext()
                                .cast(SecurityContext.class)
                                .flatMapMany(securityContext -> {
                                    System.out.println("Reactive auth: " + securityContext.getAuthentication());

                                    return bookService.reactiveSearchBook(searchType, searchWord)
                                            .delayElements(Duration.ofMillis(500)) // optional: simulate streaming
                                            .map(book -> ServerSentEvent.<GoogleBookDto>builder()
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
                                                            .map(book -> ServerSentEvent.<GoogleBookDto>builder()
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
