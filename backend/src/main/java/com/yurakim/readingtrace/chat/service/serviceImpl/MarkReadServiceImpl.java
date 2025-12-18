package com.yurakim.readingtrace.chat.service.serviceImpl;

import com.yurakim.readingtrace.chat.dto.MarkReadDto;
import com.yurakim.readingtrace.chat.entity.MarkRead;
import com.yurakim.readingtrace.chat.repository.MarkReadRepository;
import com.yurakim.readingtrace.chat.service.MarkReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MarkReadServiceImpl implements MarkReadService {

    private final MarkReadRepository markReadRepository;

    @Override
    public void saveMarkRead(MarkReadDto markReadDto) {
        markReadRepository.save(MarkRead.builder()
                .senderId(markReadDto.getSenderId())
                .receiverId(markReadDto.getReceiverId())
                .lastReadAt(markReadDto.getLastReadAt())
                .build()
        );
    }
    
}
