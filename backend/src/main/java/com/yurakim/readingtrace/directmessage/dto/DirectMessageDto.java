package com.yurakim.readingtrace.directmessage.dto;

import com.yurakim.readingtrace.directmessage.entity.DirectMessage;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DirectMessageDto {

    Long dmId;
    Long senderId;
    Long receiverId;
    String message;
    LocalDateTime createdAt;

    public static DirectMessageDto from(DirectMessage directMessage) {
        return DirectMessageDto.builder()
                .dmId(directMessage.getId())
                .message(directMessage.getMessage())
                .receiverId(directMessage.getReceiverId())
                .senderId(directMessage.getSenderId())
                .createdAt(directMessage.getCreatedAt())
                .build();
    }

}
