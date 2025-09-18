package com.yurakim.readingtrace.book.controller;

import com.yurakim.readingtrace.book.dto.UserBookDto;
import com.yurakim.readingtrace.book.service.BookService;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPath.BOOK)
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/add")
    public ResponseEntity<String> addUserBook(@RequestBody UserBookDto userBookDto) {
        bookService.addUserBook(userBookDto);
        return ResponseEntity.ok().build();
    }

}
