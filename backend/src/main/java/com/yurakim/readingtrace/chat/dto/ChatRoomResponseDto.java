package com.yurakim.readingtrace.chat.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatRoomResponseDto {
    private Long chatRoomId;
    private String chatRoomName;
    private Long ownerUserId;
    private String ownerNickname;
    private String ownerProfileImageUrl;
}
