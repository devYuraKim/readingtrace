package com.yurakim.readingtrace.directmessage.service.serviceImpl;

import com.yurakim.readingtrace.directmessage.dto.DirectMessageDto;
import com.yurakim.readingtrace.directmessage.entity.DirectMessage;
import com.yurakim.readingtrace.directmessage.repository.DirectMessageRepository;
import com.yurakim.readingtrace.directmessage.service.DirectMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DirectMessageServiceImpl implements DirectMessageService {

    private final DirectMessageRepository directMessageRepository;

    @Override
    public List<DirectMessageDto> getChunkedDirectMessages(Long senderId, Long receiverId, int limit, int offset) {
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("createdAt").ascending());
        List<DirectMessage> directMessages = directMessageRepository.findChunkedConversationBetweenUsers(senderId, receiverId, pageable);
        return directMessages.stream().map(dm -> DirectMessageDto.builder()
                .dmId(dm.getId())
                .message(dm.getMessage())
                .senderId(dm.getSenderId())
                .receiverId(dm.getReceiverId())
                .createdAt(dm.getCreatedAt())
                .build()).toList();
    }

    @Override
    public void saveDirectMessage(DirectMessageDto directMessageDto) {
        DirectMessage directMessage = DirectMessage.builder()
                .senderId(directMessageDto.getSenderId())
                .receiverId(directMessageDto.getReceiverId())
                .message(directMessageDto.getMessage())
                .isDeleted(false)
                .build();
        directMessageRepository.save(directMessage);
    }

    @Override
    public List<DirectMessageDto> getAllDirectMessages(Long senderId, Long receiverId) {
        List<DirectMessage> directMessages = directMessageRepository.findConversationBetweenUsers(senderId, receiverId);
        return directMessages.stream().map(dm -> {
            return DirectMessageDto.builder()
                    .dmId(dm.getId())
                    .message(dm.getMessage())
                    .senderId(dm.getSenderId())
                    .receiverId(dm.getReceiverId())
                    .createdAt(dm.getCreatedAt())
                    .build();
        }).toList();
    }

}
