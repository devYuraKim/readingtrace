package com.yurakim.readingtrace.directmessage.entity;

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
    private Long scrolledUserId;

    @Column(nullable = false)
    private Long notifiedUserId;

    private LocalDateTime scrolledAt;

    @Builder
    public MarkRead(Long scrolledUserId, Long notifiedUserId, LocalDateTime scrolledAt) {
        this.scrolledUserId = scrolledUserId;
        this.notifiedUserId = notifiedUserId;
        this.scrolledAt = scrolledAt;
    }

}
