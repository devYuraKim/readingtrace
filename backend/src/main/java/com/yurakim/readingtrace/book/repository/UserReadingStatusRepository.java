package com.yurakim.readingtrace.book.repository;

import com.yurakim.readingtrace.book.entity.UserReadingStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserReadingStatusRepository extends JpaRepository<UserReadingStatus, Long>, JpaSpecificationExecutor<UserReadingStatus> {

    Optional<UserReadingStatus> findByUserIdAndBookId(Long userId, Long bookId);
    Optional<UserReadingStatus> findById(Long userReadingStatusId);
    Optional<UserReadingStatus> findByUserIdAndShelfId(Long userId, Long shelfId);
    boolean existsByUserIdAndBookIdAndShelfId(Long userId, Long bookId, Long shelfId);
    Long countAllByUserIdAndStatus(Long userId, String status);
    Long countAllByUserId(Long userId);

    @EntityGraph(attributePaths = {"book"})
    List<UserReadingStatus> findAllByUserIdIn(Set<Long> userIds);

    @Query("SELECT rs.book.id FROM UserReadingStatus rs WHERE rs.userId = :userId AND rs.book.id IN :bookIds")
    Set<Long> findByUserIdAndBookIdsIn(Long userId, Set<Long> bookIds);

}
