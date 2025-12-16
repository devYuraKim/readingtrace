package com.yurakim.readingtrace.chat.repository;

import com.yurakim.readingtrace.chat.entity.DirectMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectMessageRepository extends JpaRepository<DirectMessage, Long> {



}
