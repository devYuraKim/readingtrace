package com.yurakim.readingtrace.book.service;

import com.yurakim.readingtrace.book.dto.GoogleBookDto;
import com.yurakim.readingtrace.book.dto.BookSearchResultDto;
import com.yurakim.readingtrace.book.dto.UserBookDto;
import reactor.core.publisher.Flux;

import java.util.List;

public interface BookService {

    void addUserBook(UserBookDto userBookDto);
    UserBookDto getUserBook(Long userId, Long bookId);
    UserBookDto updateUserBook(UserBookDto userBookDto);
    void deleteUserBook(Long userId, Long bookId);

    List<UserBookDto> getUserBooks(Long userId, Long shelfId, String status, String visibility, Integer rating);

    BookSearchResultDto searchBook(String searchType, String searchWord, int startIndex, int booksPerPage);

    Flux<GoogleBookDto> reactiveSearchBook(String searchType, String searchWord);
}