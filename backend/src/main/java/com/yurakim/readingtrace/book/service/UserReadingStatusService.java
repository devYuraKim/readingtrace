package com.yurakim.readingtrace.book.service;

import com.yurakim.readingtrace.book.dto.UserReadingStatusDto;
import com.yurakim.readingtrace.book.entity.UserReadingStatus;

import java.util.List;

public interface UserReadingStatusService {

    void createUserReadingStatus(UserReadingStatusDto ubsDto);
    UserReadingStatusDto getUserReadingStatus(Long userId, Long bookId);
    UserReadingStatus getUserReadingStatus(Long  userReadingStatusId);
    UserReadingStatusDto updateUserReadingStatus(UserReadingStatusDto userReadingStatusDto);
    void deleteUserReadingStatus(Long userId, Long bookId);

    List<UserReadingStatusDto> getUserReadingStatuses(Long userId, Long shelfId, String status, String visibility, Integer rating);

}
