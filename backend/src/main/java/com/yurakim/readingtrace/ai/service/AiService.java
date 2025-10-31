package com.yurakim.readingtrace.ai.service;

import com.yurakim.readingtrace.ai.dto.ChatRecordDto;
import com.yurakim.readingtrace.ai.dto.UserMessageDto;

import java.util.List;

public interface AiService {

    ChatRecordDto generateChatModelResponse(UserMessageDto userMessageDto, Long userId, String chatModel);
    List<ChatRecordDto> getChatRecords(Long userId, Long bookId);

}
