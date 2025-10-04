package com.yurakim.readingtrace.book.service;

import com.yurakim.readingtrace.book.dto.BookDto;
import com.yurakim.readingtrace.book.dto.BookSearchResultDto;
import com.yurakim.readingtrace.book.dto.UserBookDto;
import reactor.core.publisher.Flux;

import java.util.List;

public interface BookService {

    void addUserBook(UserBookDto userBookDto);
    UserBookDto getUserBook(Long userId, String bookId);

    List<UserBookDto> getUserBooks(Long userId, String status, String visibility, Integer rating);

    BookSearchResultDto searchBook(String searchType, String searchWord, int startIndex, int booksPerPage);

    Flux<BookDto> reactiveSearchBook(String searchType, String searchWord);
}
