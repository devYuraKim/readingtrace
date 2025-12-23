package com.yurakim.readingtrace.directmessage.entity;

import com.yurakim.readingtrace.shared.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class DirectMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long receiverId;

    @NotBlank
    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Builder
    public DirectMessage(Long senderId, Long receiverId, String message, Boolean isDeleted) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.isDeleted = isDeleted;
    }

}