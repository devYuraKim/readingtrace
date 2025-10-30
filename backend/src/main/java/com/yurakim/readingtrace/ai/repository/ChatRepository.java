package com.yurakim.readingtrace.ai.repository;

import com.yurakim.readingtrace.ai.entity.ChatRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<ChatRecord, Long> {

}
