package com.yurakim.readingtrace.book.repository;

import com.yurakim.readingtrace.book.entity.UserReadingProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserReadingProgressRepository extends JpaRepository<UserReadingProgress, Long> {

    Optional<UserReadingProgress> findTopByUserIdAndBookIdOrderByCreatedAtDesc(Long userId, Long bookId);

    List<UserReadingProgress> findAllByUserIdAndBookIdOrderByCreatedAtDesc(Long userId, Long bookId);
}
