package com.yurakim.readingtrace.ai.repository;

import com.yurakim.readingtrace.ai.entity.ChatRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatRecord, Long> {

    List<ChatRecord> findAllByUserIdAndBookId(Long userId, Long bookId);

}
