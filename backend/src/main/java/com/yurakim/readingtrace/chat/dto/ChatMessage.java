package com.yurakim.readingtrace.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ChatMessage {
    private String sender;
    private String recipient;
    private String content;
    private String type; //CHAT, JOIN, LEAVE, etc
}
