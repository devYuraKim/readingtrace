package com.yurakim.readingtrace.book.controller;

import com.yurakim.readingtrace.book.dto.UserBookStatusDto;
import com.yurakim.readingtrace.book.service.BookService;
import com.yurakim.readingtrace.book.service.UserBookStatusService;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPath.USERBOOK) // /api/v1/users/{userId}/books
public class UserBookController {

    private final BookService bookService;
    private final UserBookStatusService userBookStatusService;

    @GetMapping("/{bookId}")
    public ResponseEntity<UserBookStatusDto> getUserBookStatus(@PathVariable Long userId, @PathVariable Long bookId){
        UserBookStatusDto userBookStatusDto = userBookStatusService.getUserBookStatus(userId, bookId);
        return ResponseEntity.ok(userBookStatusDto);
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<Void> createUserBookStatus(@PathVariable Long userId, @PathVariable Long bookId, @RequestBody UserBookStatusDto userBookStatusDto){
        //let the path variables be the single source of truth
        userBookStatusDto.setUserId(userId);
        userBookStatusDto.setBookId(bookId);
        userBookStatusService.addUserBookStatus(userBookStatusDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{bookId}")
    public ResponseEntity<UserBookStatusDto> updateUserBookStatus(@PathVariable Long userId, @PathVariable Long bookId, @RequestBody UserBookStatusDto userBookStatusDto){
        userBookStatusDto.setUserId(userId);
        userBookStatusDto.setBookId(bookId);
        UserBookStatusDto resultDto = userBookStatusService.updateUserBookStatus(userBookStatusDto);
        return ResponseEntity.ok(resultDto);
    }

    @DeleteMapping("{bookId}")
    public ResponseEntity<Void> deleteUserBookStatus(@PathVariable Long userId, @PathVariable Long bookId){
        userBookStatusService.deleteUserBookStatus(userId, bookId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<UserBookStatusDto>> getUserBooksStatus(@PathVariable Long userId, @RequestParam Long shelfId){
        List<UserBookStatusDto> resultDtoList = userBookStatusService.getUserBooksStatus(userId, shelfId, null, null, null);
        return ResponseEntity.ok(resultDtoList);
    }
}
