package com.yurakim.readingtrace.directmessage.repository;

import com.yurakim.readingtrace.directmessage.entity.DirectMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DirectMessageRepository extends JpaRepository<DirectMessage, Long> {

    @Query("SELECT dm FROM DirectMessage dm WHERE " +
            "(dm.senderId = :userId1 AND dm.receiverId = :userId2) OR " +
            "(dm.senderId = :userId2 AND dm.receiverId = :userId1)")
    List<DirectMessage> findConversationBetweenUsers(
            @Param("userId1") Long userId1,
            @Param("userId2") Long userId2
    );
}
