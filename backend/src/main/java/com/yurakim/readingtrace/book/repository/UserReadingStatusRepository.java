package com.yurakim.readingtrace.book.repository;

import com.yurakim.readingtrace.book.entity.UserReadingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserReadingStatusRepository extends JpaRepository<UserReadingStatus, Long>, JpaSpecificationExecutor<UserReadingStatus> {

    Optional<UserReadingStatus> findByUserIdAndBookId(Long userId, Long bookId);
    Optional<UserReadingStatus> findByUserIdAndShelfId(Long userId, Long shelfId);
    boolean existsByUserIdAndBookIdAndShelfId(Long userId, Long bookId, Long shelfId);

}
