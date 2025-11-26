package com.yurakim.readingtrace.chat.service.serviceImpl;

import com.yurakim.readingtrace.chat.entity.ChatRoom;
import com.yurakim.readingtrace.chat.repository.ChatRoomRepository;
import com.yurakim.readingtrace.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;

    @Override
    public void createChatRoom(Long userId, String chatRoomName) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setUserId(userId);
        chatRoom.setChatRoomName(chatRoomName);
        chatRoomRepository.save(chatRoom);
    }
}
