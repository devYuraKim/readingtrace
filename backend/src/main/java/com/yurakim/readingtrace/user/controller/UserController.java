package com.yurakim.readingtrace.user.controller;

import com.yurakim.readingtrace.auth.dto.LoginResponseDto;
import com.yurakim.readingtrace.book.dto.UserBookDto;
import com.yurakim.readingtrace.book.service.BookService;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import com.yurakim.readingtrace.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(ApiPath.USER)
public class UserController {

    private final UserService userService;
    private final BookService bookService;

    @PostMapping("/{userId}/books/{bookId}")
    public ResponseEntity<UserBookDto> addUserBook(@PathVariable Long userId, @PathVariable String bookId, @RequestBody UserBookDto userBookDto){
        //let the path variables be the single source of truth
        userBookDto.setUserId(userId);
        userBookDto.setBookId(bookId);
        bookService.addUserBook(userBookDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/books/{bookId}")
    public ResponseEntity<UserBookDto> getUserBook(@PathVariable Long userId, @PathVariable String bookId){
        UserBookDto userBookDto = bookService.getUserBook(userId, bookId);
        return ResponseEntity.ok(userBookDto);
    }


    @GetMapping("/{id}")
    public ResponseEntity<LoginResponseDto> getUser(@PathVariable("id") Long id, @AuthenticationPrincipal String email){
        LoginResponseDto loginResponseDto = userService.getUser(id, email);
        return ResponseEntity.ok(loginResponseDto);
    }
}
