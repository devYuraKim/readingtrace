package com.yurakim.readingtrace.chat.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class MarkRead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long receiverId;

    private LocalDateTime lastReadAt;

    @Builder
    public MarkRead(Long senderId, Long receiverId, LocalDateTime lastReadAt) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.lastReadAt = lastReadAt;
    }

}
