package com.yurakim.readingtrace.chat.service.serviceImpl;

import com.yurakim.readingtrace.chat.dto.DirectMessageDto;
import com.yurakim.readingtrace.chat.entity.DirectMessage;
import com.yurakim.readingtrace.chat.repository.DirectMessageRepository;
import com.yurakim.readingtrace.chat.service.DirectMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DirectMessageServiceImpl implements DirectMessageService {

    private final DirectMessageRepository directMessageRepository;

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
}
