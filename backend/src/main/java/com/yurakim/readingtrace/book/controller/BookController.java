package com.yurakim.readingtrace.book.controller;

import com.yurakim.readingtrace.book.dto.UserBookDto;
import com.yurakim.readingtrace.book.service.BookService;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
