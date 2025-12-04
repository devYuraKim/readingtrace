package com.yurakim.readingtrace.book.service.serviceImpl;

import com.yurakim.readingtrace.book.dto.UserReadingProgressRequestDto;
import com.yurakim.readingtrace.book.entity.UserReadingProgress;
import com.yurakim.readingtrace.book.repository.UserReadingProgressRepository;
import com.yurakim.readingtrace.book.service.UserReadingProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserReadingProgressServiceImpl implements UserReadingProgressService {

    private final UserReadingProgressRepository userReadingProgressRepository;

    @Override
    public void createUserReadingProgress(UserReadingProgressRequestDto userReadingProgressRequestDto) {
        UserReadingProgress userReadingProgress = new UserReadingProgress();
        userReadingProgress.setUserId(userReadingProgressRequestDto.getUserId());
        userReadingProgress.setBookId(userReadingProgressRequestDto.getBookId());
        userReadingProgress.setUserReadingStatusId(userReadingProgressRequestDto.getUserReadingStatusId());
        userReadingProgress.setCurrentPage(userReadingProgressRequestDto.getCurrentPage());
    }
}
