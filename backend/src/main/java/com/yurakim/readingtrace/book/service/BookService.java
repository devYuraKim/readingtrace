package com.yurakim.readingtrace.book.service;

import com.yurakim.readingtrace.book.dto.BookDto;
import com.yurakim.readingtrace.book.dto.UserBookDto;
import reactor.core.publisher.Flux;

import java.util.List;

public interface BookService {

    void addUserBook(UserBookDto userBookDto);

    List<UserBookDto> getUserBooks(Long userId, String status, String visibility, Integer rating);

    Flux<BookDto> reactiveSearchBook(String searchType, String searchWord);
}
