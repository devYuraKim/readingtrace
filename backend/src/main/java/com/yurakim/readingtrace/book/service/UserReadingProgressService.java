package com.yurakim.readingtrace.book.service;

import com.yurakim.readingtrace.book.dto.UserReadingProgressRequestDto;
import com.yurakim.readingtrace.book.dto.UserReadingProgressResponseDto;

import java.util.List;

public interface UserReadingProgressService {

    void createUserReadingProgress(UserReadingProgressRequestDto userReadingProgressRequestDto);

    List<UserReadingProgressResponseDto> getUserReadingProgresses(Long userId, Long bookId);

    Integer getUserReadingProgress(Long userId, Long bookId);

    void completeUserReadingProgress(UserReadingProgressRequestDto userReadingProgressRequestDto);

    void deleteUserReadingProgress(Long userReadingProgressId);

}
