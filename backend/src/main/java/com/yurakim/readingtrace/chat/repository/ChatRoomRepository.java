package com.yurakim.readingtrace.chat.repository;

import com.yurakim.readingtrace.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
