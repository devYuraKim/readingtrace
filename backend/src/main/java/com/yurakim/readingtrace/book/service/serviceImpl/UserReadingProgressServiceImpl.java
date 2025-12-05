package com.yurakim.readingtrace.book.service.serviceImpl;

import com.yurakim.readingtrace.book.dto.UserReadingProgressRequestDto;
import com.yurakim.readingtrace.book.dto.UserReadingProgressResponseDto;
import com.yurakim.readingtrace.book.entity.UserReadingProgress;
import com.yurakim.readingtrace.book.entity.UserReadingStatus;
import com.yurakim.readingtrace.book.repository.UserReadingProgressRepository;
import com.yurakim.readingtrace.book.service.UserReadingProgressService;
import com.yurakim.readingtrace.book.service.UserReadingStatusService;
import com.yurakim.readingtrace.shelf.enums.DefaultShelfType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class UserReadingProgressServiceImpl implements UserReadingProgressService {

    private final UserReadingProgressRepository userReadingProgressRepository;
    private final UserReadingStatusService userReadingStatusService;

    @Override
    public void createUserReadingProgress(UserReadingProgressRequestDto userReadingProgressRequestDto) {
        UserReadingProgress userReadingProgress = new UserReadingProgress();
        userReadingProgress.setUserId(userReadingProgressRequestDto.getUserId());
        userReadingProgress.setBookId(userReadingProgressRequestDto.getBookId());
        userReadingProgress.setUserReadingStatusId(userReadingProgressRequestDto.getUserReadingStatusId());
        userReadingProgress.setCurrentPage(userReadingProgressRequestDto.getCurrentPage());
        userReadingProgressRepository.save(userReadingProgress);
    }

    @Override
    public List<UserReadingProgressResponseDto> getUserReadingProgresses(Long userId, Long bookId) {
        List<UserReadingProgress> userReadingProgresses = userReadingProgressRepository.findAllByUserIdAndBookIdOrderByCreatedAtDesc(userId, bookId);
        return userReadingProgresses.stream().map(data -> {
            UserReadingProgressResponseDto dto = new UserReadingProgressResponseDto();
            dto.setUserReadingProgressId(data.getId());
            dto.setUserId(data.getUserId());
            dto.setBookId(data.getBookId());
            dto.setUserReadingStatusId(data.getUserReadingStatusId());
            dto.setCurrentPage(data.getCurrentPage());
            dto.setUserReadingProgressCreatedAt(data.getCreatedAt());
            return dto;
        }).toList();
    }

    @Override
    public Integer getUserReadingProgress(Long userId, Long bookId) {
        UserReadingProgress userReadingProgress = userReadingProgressRepository.findTopByUserIdAndBookIdOrderByCreatedAtDesc(userId, bookId).orElseThrow(()->new RuntimeException("No reading progress found with bookId: " + bookId + " and userId: " + userId));
        return userReadingProgress.getCurrentPage();
    }

    @Override
    public void completeUserReadingProgress(UserReadingProgressRequestDto userReadingProgressRequestDto) {
        UserReadingStatus userReadingStatus = userReadingStatusService.getUserReadingStatus(userReadingProgressRequestDto.getUserReadingStatusId());
        userReadingStatus.setEndDate(new Date());
        userReadingStatus.setStatus(DefaultShelfType.ALREADY_READ.getSlug());

    }

    @Override
    public void deleteUserReadingProgress(Long userReadingProgressId) {
        userReadingProgressRepository.deleteById(userReadingProgressId);
    }
}
