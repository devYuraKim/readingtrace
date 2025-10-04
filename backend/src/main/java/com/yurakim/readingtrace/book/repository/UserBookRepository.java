package com.yurakim.readingtrace.book.repository;

import com.yurakim.readingtrace.book.entity.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserBookRepository extends JpaRepository<UserBook, Long>, JpaSpecificationExecutor<UserBook> {

    UserBook findByUserIdAndBookId(Long userId, String bookId);

}
