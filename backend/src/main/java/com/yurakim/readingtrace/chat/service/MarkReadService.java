package com.yurakim.readingtrace.chat.service;

import com.yurakim.readingtrace.chat.dto.MarkReadDto;

public interface MarkReadService {

    void saveMarkRead(MarkReadDto markReadDto);

}