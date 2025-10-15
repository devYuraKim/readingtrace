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

    @GetMapping("/{id}")
    public ResponseEntity<LoginResponseDto> getUser(@PathVariable("id") Long id, @AuthenticationPrincipal String email){
        LoginResponseDto loginResponseDto = userService.getUser(id, email);
        return ResponseEntity.ok(loginResponseDto);
    }

//    UserBook
    @PostMapping("/{userId}/books/{bookId}")
    public ResponseEntity<Void> addUserBook(@PathVariable Long userId, @PathVariable String bookId, @RequestBody UserBookDto userBookDto){
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

    @PutMapping("/{userId}/books/{bookId}")
    public ResponseEntity<UserBookDto> updateUserBook(@PathVariable Long userId, @PathVariable String bookId, @RequestBody UserBookDto userBookDto){
        userBookDto.setUserId(userId);
        userBookDto.setBookId(bookId);
        UserBookDto resultDto = bookService.updateUserBook(userBookDto);
        return ResponseEntity.ok(resultDto);
    }

    @DeleteMapping("/{userId}/books/{bookId}")
    public ResponseEntity<Void> deleteUserBook(@PathVariable Long userId, @PathVariable String bookId){
        bookService.deleteUserBook(userId, bookId);
        return ResponseEntity.ok().build();
    }

}
