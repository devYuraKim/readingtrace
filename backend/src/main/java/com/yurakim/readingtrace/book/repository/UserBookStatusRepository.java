package com.yurakim.readingtrace.book.repository;

import com.yurakim.readingtrace.book.entity.UserBookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserBookStatusRepository extends JpaRepository<UserBookStatus, Long>, JpaSpecificationExecutor<UserBookStatus> {

    UserBookStatus findByUserIdAndBookId(Long userId, Long bookId);

}
