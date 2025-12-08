package com.yurakim.readingtrace.book.controller;

import com.yurakim.readingtrace.book.dto.*;
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
    public ResponseEntity<UserReadingStatusDto> createUserReadingStatus(@PathVariable Long userId, @RequestBody UserBookDto userBookDto) {
        //Separate BookDto fields from UserBookDto
        BookDto bookDto = userBookMapper.extractBookDto(userBookDto);
        BookDto resultBookDto = bookService.createOrGetBook(bookDto);
        //Set UserId(let URL path variable be single source of truth)
        userBookDto.setUserId(userId);
        //Separate UserReadingStatus from UserBookDto and set BookId(let resultBookDto be single source of truth)

        UserReadingStatusDto ursDto =  new UserReadingStatusDto();
        ursDto.setUserReadingStatusId(userBookDto.getUserReadingStatusId());
        ursDto.setUserId(userBookDto.getUserId());
        ursDto.setShelfId(userBookDto.getShelfId());
        ursDto.setBookDto(resultBookDto);
        ursDto.setStatus(userBookDto.getStatus());
        ursDto.setVisibility(userBookDto.getVisibility());
        ursDto.setRating(userBookDto.getRating());
        ursDto.setStartDate(userBookDto.getStartDate());
        ursDto.setEndDate(userBookDto.getEndDate());
        ursDto.setUserReadingStatusCreatedAt(ursDto.getUserReadingStatusCreatedAt());
        ursDto.setUserReadingStatusUpdatedAt(ursDto.getUserReadingStatusUpdatedAt());
        userReadingStatusService.createUserReadingStatus(ursDto);

        return ResponseEntity.ok().body(ursDto);
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
    public ResponseEntity<UserReadingStatusDto> updateUserReadingStatus(@PathVariable Long userId, @PathVariable Long bookId, @RequestBody UserBookDto userBookDto){
        //Separate Book from UserBookDto
        BookDto bookDto = userBookMapper.extractBookDto(userBookDto);
        //Set userId and bookId with PathVariable as the single source of truth
        userBookDto.setUserId(userId);
        userBookDto.setBookId(bookId);
        //Separate UserReadingStatus from UserBookDto
        UserReadingStatusDto ursDto =  new UserReadingStatusDto();
        ursDto.setUserReadingStatusId(userBookDto.getUserReadingStatusId());
        ursDto.setUserId(userBookDto.getUserId());
        ursDto.setShelfId(userBookDto.getShelfId());
        ursDto.setBookDto(bookDto);
        ursDto.setStatus(userBookDto.getStatus());
        ursDto.setVisibility(userBookDto.getVisibility());
        ursDto.setRating(userBookDto.getRating());
        ursDto.setStartDate(userBookDto.getStartDate());
        ursDto.setEndDate(userBookDto.getEndDate());
        ursDto.setUserReadingStatusCreatedAt(ursDto.getUserReadingStatusCreatedAt());
        ursDto.setUserReadingStatusUpdatedAt(ursDto.getUserReadingStatusUpdatedAt());
        userReadingStatusService.createUserReadingStatus(ursDto);

        //Update UserReadingStatus
        UserReadingStatusDto updatedURSDto = userReadingStatusService.updateUserReadingStatus(ursDto);
        return ResponseEntity.ok(updatedURSDto);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteUserBook(@PathVariable Long userId, @PathVariable Long bookId){
        userReadingStatusService.deleteUserReadingStatus(userId, bookId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<UserBookDto>> getUserBooks(@PathVariable Long userId,
                                                          @RequestParam(required = false) Long shelfId,
                                                          @RequestParam(required = false) String shelfSlug
    ) {

        List<UserReadingStatusDto> ursDtoList = userReadingStatusService.getUserReadingStatuses(userId, shelfId, null, null, null);
        if (shelfId != null) {
            ursDtoList = userReadingStatusService.getUserReadingStatuses(userId, shelfId, null, null, null);
        } else if (shelfSlug != null) {
            if(shelfSlug.toLowerCase().equals("all")) {
                ursDtoList = userReadingStatusService.getUserReadingStatuses(userId, null, null, null, null);
            } else {
            ursDtoList = userReadingStatusService.getUserReadingStatuses(userId, null, shelfSlug, null, null);
            }
        } else {
            ursDtoList = userReadingStatusService.getUserReadingStatuses(userId, null, null, null, null);
        }
        //TODO: separate this logic to Service layer
        List<UserBookDto> userBookDtoList = ursDtoList.stream().map(ursDto -> {
            BookDto bookDto = bookService.getBookById(ursDto.getBookDto().getBookId());
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

    @GetMapping("/{bookId}/progress")
    public ResponseEntity<List<UserReadingProgressResponseDto>> getUserReadingProgresses(@PathVariable Long userId, @PathVariable Long bookId) {
       List<UserReadingProgressResponseDto> userReadingProgresses = userReadingProgressService.getUserReadingProgresses(userId, bookId);
       return ResponseEntity.ok(userReadingProgresses);
    }

    @GetMapping("/current-progress")
    public ResponseEntity<Integer> getUserReadingProgress(@PathVariable Long userId, @RequestParam Long bookId) {
        Integer currentPage = userReadingProgressService.getUserReadingProgress(userId, bookId);
        return ResponseEntity.ok(currentPage);
    }

    @PostMapping("/complete-progress")
    public ResponseEntity<Void> completeUserReadingProgress(@PathVariable Long userId, @RequestBody UserReadingProgressRequestDto userReadingProgressRequestDto) {
        userReadingProgressRequestDto.setUserId(userId);
        userReadingProgressService.createUserReadingProgress(userReadingProgressRequestDto);
        userReadingProgressService.completeUserReadingProgress(userReadingProgressRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{bookId}/progress")
    public ResponseEntity<Void> deleteUserReadingProgress(@PathVariable Long userId, @PathVariable Long bookId, @RequestParam Long id) {
        userReadingProgressService.deleteUserReadingProgress(id);
        return ResponseEntity.ok().build();
    }


}
