package com.yurakim.readingtrace.chat.dto;

import lombok.*;

@ToString
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class DirectMessageDto {

    Long senderId;
    Long receiverId;
    String message;

}
