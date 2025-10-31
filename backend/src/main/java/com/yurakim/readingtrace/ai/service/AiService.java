package com.yurakim.readingtrace.ai.service;

import com.yurakim.readingtrace.ai.dto.ChatRecordDto;
import com.yurakim.readingtrace.ai.dto.UserMessageDto;

public interface AiService {

    ChatRecordDto generateChatModelResponse(UserMessageDto userMessageDto, Long userId, String chatModel);
}
