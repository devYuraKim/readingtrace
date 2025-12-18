package com.yurakim.readingtrace.chat.repository;

import com.yurakim.readingtrace.chat.entity.MarkRead;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarkReadRepository extends JpaRepository<MarkRead, Long> {

}
