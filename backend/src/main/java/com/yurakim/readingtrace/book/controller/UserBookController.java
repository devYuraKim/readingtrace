package com.yurakim.readingtrace.book.controller;

import com.yurakim.readingtrace.book.dto.UserBookDto;
import com.yurakim.readingtrace.book.service.BookService;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPath.USERBOOK) // /api/v1/users/{userId}/books
public class UserBookController {

    private final BookService bookService;

    //    UserBook
    @PostMapping("/{bookId}")
    public ResponseEntity<Void> addUserBook(@PathVariable Long userId, @PathVariable String bookId, @RequestBody UserBookDto userBookDto){
        //let the path variables be the single source of truth
        userBookDto.setUserId(userId);
        userBookDto.setBookId(bookId);
        bookService.addUserBook(userBookDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<UserBookDto> getUserBook(@PathVariable Long userId, @PathVariable String bookId){
        UserBookDto userBookDto = bookService.getUserBook(userId, bookId);
        return ResponseEntity.ok(userBookDto);
    }

    @PutMapping("{bookId}")
    public ResponseEntity<UserBookDto> updateUserBook(@PathVariable Long userId, @PathVariable String bookId, @RequestBody UserBookDto userBookDto){
        userBookDto.setUserId(userId);
        userBookDto.setBookId(bookId);
        UserBookDto resultDto = bookService.updateUserBook(userBookDto);
        return ResponseEntity.ok(resultDto);
    }

    @DeleteMapping("{bookId}")
    public ResponseEntity<Void> deleteUserBook(@PathVariable Long userId, @PathVariable String bookId){
        bookService.deleteUserBook(userId, bookId);
        return ResponseEntity.ok().build();
    }
}
