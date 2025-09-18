package com.yurakim.readingtrace.book.repository;

import com.yurakim.readingtrace.book.entity.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBookRepository extends JpaRepository<UserBook, Long> {

}
