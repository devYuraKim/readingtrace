package com.yurakim.readingtrace.book.repository;

import com.yurakim.readingtrace.book.entity.UserReadingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface UserReadingStatusRepository extends JpaRepository<UserReadingStatus, Long>, JpaSpecificationExecutor<UserReadingStatus> {

    Optional<UserReadingStatus> findByUserIdAndBookId(Long userId, Long bookId);
    Optional<UserReadingStatus> findByUserIdAndShelfId(Long userId, Long shelfId);
    boolean existsByUserIdAndBookIdAndShelfId(Long userId, Long bookId, Long shelfId);
    Long countAllByUserIdAndStatus(Long userId, String status);

    @Query("SELECT bookId FROM UserReadingStatus WHERE bookId IN :bookIds AND userId = :userId")
    Set<Long> findByUserIdAndBookIdsIn(Long userId, Set<Long> bookIds);

}
