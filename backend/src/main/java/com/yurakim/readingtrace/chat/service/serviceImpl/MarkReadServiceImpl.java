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
    public MarkReadDto saveMarkRead(MarkReadDto markReadDto) {
        markReadRepository.save(MarkRead.builder()
                .scrolledUserId(markReadDto.getScrolledUserId())
                .notifiedUserId(markReadDto.getNotifiedUserId())
                .scrolledAt(markReadDto.getScrolledAt())
                .build()
        );
        return markReadDto;
    }
    
}
