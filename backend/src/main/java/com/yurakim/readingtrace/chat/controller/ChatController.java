package com.yurakim.readingtrace.chat.controller;

import com.yurakim.readingtrace.chat.dto.ChatRoomDto;
import com.yurakim.readingtrace.chat.dto.ChatRoomResponseDto;
import com.yurakim.readingtrace.chat.service.ChatService;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(ApiPath.USERCHAT) // /api/v1/users/{userId}/chats
@RestController
public class ChatController {

    private final ChatService chatService;

    @PostMapping()
    public ResponseEntity<Void> createChatRoom(@PathVariable Long userId, @RequestBody ChatRoomDto chatRoomDto) {
        chatService.createChatRoom(userId, chatRoomDto.getChatRoomName());
        System.out.println("Creating chat room: " + chatRoomDto.getChatRoomName());
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<List<ChatRoomResponseDto>> getAllChatRooms(@PathVariable Long userId) {
        List<ChatRoomResponseDto> chatRooms = chatService.getAllChatRooms();
        return ResponseEntity.ok(chatRooms);
    }

}
