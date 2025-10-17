package com.yurakim.readingtrace.book.service;

import com.yurakim.readingtrace.book.dto.BookSearchResultDto;
import com.yurakim.readingtrace.book.dto.GoogleBookDto;
import reactor.core.publisher.Flux;

public interface BookService {

    BookSearchResultDto searchBook(String searchType, String searchWord, int startIndex, int booksPerPage);

    Flux<GoogleBookDto> reactiveSearchBook(String searchType, String searchWord);
}