package com.yurakim.readingtrace.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString
public class ChatMessageDto {
    private String sender;
    private String recipient;
    private String content;
    private LocalDateTime timestamp;
    private String type; //CHAT, JOIN, LEAVE, etc
}