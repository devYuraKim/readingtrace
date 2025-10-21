package com.yurakim.readingtrace.book.service;

import com.yurakim.readingtrace.book.dto.UserReadingStatusDto;

import java.util.List;

public interface UserReadingStatusService {

    void createUserReadingStatus(UserReadingStatusDto ubsDto);
    UserReadingStatusDto getUserReadingStatus(Long userId, Long bookId);
    UserReadingStatusDto updateUserReadingStatus(UserReadingStatusDto userReadingStatusDto);
    void deleteUserReadingStatus(Long userId, Long bookId);

    List<UserReadingStatusDto> getUserBooksStatus(Long userId, Long shelfId, String status, String visibility, Integer rating);

}
