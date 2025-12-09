package com.yurakim.readingtrace.chat.service;

import com.yurakim.readingtrace.chat.dto.ChatRoomResponseDto;

import java.util.List;

public interface ChatService {

    void createChatRoom(Long userId, String chatRoomName);

    List<ChatRoomResponseDto> getAllChatRooms();
}
