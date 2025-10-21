package com.yurakim.readingtrace.book.repository;

import com.yurakim.readingtrace.book.entity.UserReadingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserReadingStatusRepository extends JpaRepository<UserReadingStatus, Long>, JpaSpecificationExecutor<UserReadingStatus> {

    UserReadingStatus findByUserIdAndBookId(Long userId, Long bookId);
    UserReadingStatus findByUserIdAndShelfId(Long userId, Long shelfId);

}
