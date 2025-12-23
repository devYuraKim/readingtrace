package com.yurakim.readingtrace.directmessage.service.serviceImpl;

import com.yurakim.readingtrace.directmessage.dto.MarkReadDto;
import com.yurakim.readingtrace.directmessage.entity.MarkRead;
import com.yurakim.readingtrace.directmessage.repository.MarkReadRepository;
import com.yurakim.readingtrace.directmessage.service.MarkReadService;
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
