package com.yurakim.readingtrace.book.controller;

import com.yurakim.readingtrace.book.dto.BookDto;
import com.yurakim.readingtrace.book.dto.UserBookDto;
import com.yurakim.readingtrace.book.dto.UserReadingProgressRequestDto;
import com.yurakim.readingtrace.book.dto.UserReadingStatusDto;
import com.yurakim.readingtrace.book.mapper.UserBookMapper;
import com.yurakim.readingtrace.book.service.BookService;
import com.yurakim.readingtrace.book.service.UserReadingProgressService;
import com.yurakim.readingtrace.book.service.UserReadingStatusService;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPath.USERBOOK) // /api/v1/users/{userId}/books
public class UserBookController {

    private final UserReadingStatusService userReadingStatusService;
    private final UserReadingProgressService userReadingProgressService;
    private final UserBookMapper userBookMapper;
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<UserBookDto> createUserBook(@PathVariable Long userId, @RequestBody UserBookDto userBookDto) {
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

        UserBookDto compositeUserBookDto = userBookMapper.combineDTOs(resultBookDto, ursDto);
        return ResponseEntity.ok().body(compositeUserBookDto);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<UserBookDto> getUserBook(@PathVariable Long userId, @PathVariable Long bookId) {
        BookDto bookDto = bookService.getBookById(bookId);
        UserReadingStatusDto ursDto= userReadingStatusService.getUserReadingStatus(userId, bookId);
        UserBookDto compositeUserBookDto = userBookMapper.combineDTOs(bookDto, ursDto);
        return ResponseEntity.ok().body(compositeUserBookDto);
    }

    //TODO: resolve changing userReadingStatusId issue
    @PutMapping("/{bookId}")
    public ResponseEntity<UserBookDto> updateUserBook(@PathVariable Long userId, @PathVariable Long bookId, @RequestBody UserBookDto userBookDto){
        //Separate Book from UserBookDto
        BookDto bookDto = userBookMapper.extractBookDto(userBookDto);
        //Set userId and bookId with PathVariable as the single source of truth
        userBookDto.setUserId(userId);
        userBookDto.setBookId(bookId);
        //Separate UserReadingStatus from UserBookDto
        UserReadingStatusDto ursDto = userBookMapper.extractUserReadingStatusDto(userBookDto);
        //Update UserReadingStatus
        UserReadingStatusDto updatedURSDto = userReadingStatusService.updateUserReadingStatus(ursDto);
        //Combine Book and UserReadingStatus to return UserBook
        UserBookDto compositeUserBookDto = userBookMapper.combineDTOs(bookDto, updatedURSDto);
        return ResponseEntity.ok(compositeUserBookDto);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteUserBook(@PathVariable Long userId, @PathVariable Long bookId){
        userReadingStatusService.deleteUserReadingStatus(userId, bookId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<UserBookDto>> getUserBooks(@PathVariable Long userId,
                                                          @RequestParam(required = false) Long shelfId,
                                                          @RequestParam(required = false) String shelfSlug) {

        List<UserReadingStatusDto> ursDtoList = userReadingStatusService.getUserReadingStatuses(userId, shelfId, null, null, null);
        if (shelfId != null) {
            ursDtoList = userReadingStatusService.getUserReadingStatuses(userId, shelfId, null, null, null);
        } else if (shelfSlug != null) {
            ursDtoList = userReadingStatusService.getUserReadingStatuses(userId, null, shelfSlug, null, null);
        } else {
            throw new IllegalArgumentException("FAILED TO GET BOOKS: Either shelfId or shelfSlug must be provided");
        }
        //TODO: separate this logic to Service layer
        List<UserBookDto> userBookDtoList = ursDtoList.stream().map(ursDto -> {
            BookDto bookDto = bookService.getBookById(ursDto.getBookId());
            return userBookMapper.combineDTOs(bookDto, ursDto);
        }).toList();
        return ResponseEntity.ok(userBookDtoList);
    }

    @PostMapping("/progress")
    public ResponseEntity<Void> createUserReadingProgress(@PathVariable Long userId, @RequestBody UserReadingProgressRequestDto userReadingProgressRequestDto) {
        userReadingProgressRequestDto.setUserId(userId);
        userReadingProgressService.createUserReadingProgress(userReadingProgressRequestDto);
        return ResponseEntity.ok().build();
    }

}
