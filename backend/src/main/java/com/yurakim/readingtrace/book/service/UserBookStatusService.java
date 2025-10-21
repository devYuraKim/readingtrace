package com.yurakim.readingtrace.book.service;

import com.yurakim.readingtrace.book.dto.UserBookStatusDto;

import java.util.List;

public interface UserBookStatusService {

    void createUserBookStatus(UserBookStatusDto userBookStatusDto);
    UserBookStatusDto getUserBookStatus(Long userId, Long bookId);
    UserBookStatusDto updateUserBookStatus(UserBookStatusDto userBookStatusDto);
    void deleteUserBookStatus(Long userId, Long bookId);

    List<UserBookStatusDto> getUserBooksStatus(Long userId, Long shelfId, String status, String visibility, Integer rating);

}
