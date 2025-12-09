package com.yurakim.readingtrace.chat.service.serviceImpl;

import com.yurakim.readingtrace.chat.dto.ChatRoomResponseDto;
import com.yurakim.readingtrace.chat.entity.ChatRoom;
import com.yurakim.readingtrace.chat.repository.ChatRoomRepository;
import com.yurakim.readingtrace.chat.service.ChatService;
import com.yurakim.readingtrace.user.dto.ChatProfileDto;
import com.yurakim.readingtrace.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserProfileService userProfileService;

    @Override
    public void createChatRoom(Long userId, String chatRoomName) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setUserId(userId);
        chatRoom.setChatRoomName(chatRoomName);
        chatRoomRepository.save(chatRoom);
    }

    @Override
    public List<ChatRoomResponseDto> getAllChatRooms() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();

        Set<Long> ownerUserIds = chatRooms.stream().map(chatRoom -> chatRoom.getUserId()).collect(Collectors.toSet());
        List<ChatProfileDto> ownerChatProfiles = userProfileService.getAllChatProfilesByIds(ownerUserIds);

        return chatRooms.stream().map(chatRoom -> {
            ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto();
            chatRoomResponseDto.setChatRoomId(chatRoom.getId());
            chatRoomResponseDto.setChatRoomName(chatRoom.getChatRoomName());
            ownerChatProfiles.stream().forEach(chatProfile -> {
                chatRoomResponseDto.setOwnerUserId(chatProfile.getUserId());
                chatRoomResponseDto.setOwnerNickname(chatProfile.getNickname());
                chatRoomResponseDto.setOwnerProfileImageUrl(chatProfile.getProfileImageUrl());
            });
            return chatRoomResponseDto;
        }).toList();
    }

}