package com.yurakim.readingtrace.book.controller;

import com.yurakim.readingtrace.book.dto.BookDto;
import com.yurakim.readingtrace.book.dto.UserBookDto;
import com.yurakim.readingtrace.book.dto.UserReadingStatusDto;
import com.yurakim.readingtrace.book.service.BookService;
import com.yurakim.readingtrace.book.service.UserReadingStatusService;
import com.yurakim.readingtrace.book.mapper.UserBookMapper;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPath.USERBOOK) // /api/v1/users/{userId}/books
public class UserBookController {

    private final UserReadingStatusService userReadingStatusService;
    private final UserBookMapper userBookMapper;
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Void> createUserBook(@PathVariable Long userId, @RequestBody UserBookDto userBookDto) {
        //Separate BookDto fields from UserBookDto
        BookDto bookDto = userBookMapper.extractBookDto(userBookDto);
        //TODO: check if need to return BookDto, because this method's return type is 'VOID'
        BookDto resultBookDto = bookService.createOrGetBook(bookDto);
        //Set UserId(let URL path variable be single source of truth)
        userBookDto.setUserId(userId);
        //Separate UserReadingStatus from UserBookDto and set BookId(let resultBookDto be single source of truth)
        UserReadingStatusDto ursDto = userBookMapper.extractUserReadingStatusDto(userBookDto);
        ursDto.setBookId(resultBookDto.getBookId());
        userReadingStatusService.createUserReadingStatus(ursDto);
        //TODO: check if need to return UserBookDto
        return ResponseEntity.ok().build();
    }

    //@PostMapping("/{bookId}")
    //public ResponseEntity<Void> createUserReadingStatus(@PathVariable Long userId, @PathVariable Long bookId, @RequestBody UserReadingStatusDto userReadingStatusDto){
    //    //let the path variables be the single source of truth
    //    userReadingStatusDto.setUserId(userId);
    //    userReadingStatusDto.setBookId(bookId);
    //    userReadingStatusService.createUserReadingStatus(userReadingStatusDto);
    //    return ResponseEntity.ok().build();
    //}

    //UserReadingStatus
//    //TODO: should return both UserReadingStatus and Book
//    @GetMapping("/{bookId}")
//    public ResponseEntity<UserBookDto> getUserReadingStatus(@PathVariable Long userId, @PathVariable Long bookId){
//        BookDto bookDto = bookService.getBookById(bookId);
//        UserReadingStatusDto ursDto = userReadingStatusService.getUserReadingStatus(userId, bookId);
//        UserBookDto combinedUserBookDto = userBookMapper.combineDTOs(bookDto, ursDto);
//        return ResponseEntity.ok(combinedUserBookDto);
//    }


    @PutMapping("{bookId}")
    public ResponseEntity<UserReadingStatusDto> updateUserReadingStatus(@PathVariable Long userId, @PathVariable Long bookId, @RequestBody UserReadingStatusDto userReadingStatusDto){
        userReadingStatusDto.setUserId(userId);
        userReadingStatusDto.setBookId(bookId);
        UserReadingStatusDto resultDto = userReadingStatusService.updateUserReadingStatus(userReadingStatusDto);
        return ResponseEntity.ok(resultDto);
    }

    @DeleteMapping("{bookId}")
    public ResponseEntity<Void> deleteUserReadingStatus(@PathVariable Long userId, @PathVariable Long bookId){
        userReadingStatusService.deleteUserReadingStatus(userId, bookId);
        return ResponseEntity.ok().build();
    }

//    //UserBook
//    @GetMapping
//    public ResponseEntity<List<UserReadingStatusDto>> getUserBooksStatus(@PathVariable Long userId, @RequestParam Long shelfId){
////        List<UserReadingStatusDto> resultDtoList = userReadingStatusService.getUserBooksStatus(userId, shelfId, null, null, null);
////        return ResponseEntity.ok(resultDtoList);
//        List<UserBookDto> userBooks = userBookService.getUserBooks(userId, shelfId);
//        return null;
//    }
}
