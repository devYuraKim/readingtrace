package com.yurakim.readingtrace.book.service;

import com.yurakim.readingtrace.book.dto.BookDto;
import com.yurakim.readingtrace.book.dto.GoogleBooksSearchResultDto;
import reactor.core.publisher.Flux;

public interface BookService {


    GoogleBooksSearchResultDto searchBook(String searchType, String searchWord, int startIndex, int booksPerPage);

    Flux<BookDto> reactiveSearchBook(String searchType, String searchWord);
}