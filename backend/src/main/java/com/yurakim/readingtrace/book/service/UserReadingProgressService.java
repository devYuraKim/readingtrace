package com.yurakim.readingtrace.book.service;

import com.yurakim.readingtrace.book.dto.UserReadingProgressRequestDto;

public interface UserReadingProgressService {

    void createUserReadingProgress(UserReadingProgressRequestDto userReadingProgressRequestDto);

}
