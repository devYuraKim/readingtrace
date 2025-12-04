package com.yurakim.readingtrace.book.repository;

import com.yurakim.readingtrace.book.entity.UserReadingProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReadingProgressRepository extends JpaRepository<UserReadingProgress, Long> {
}
