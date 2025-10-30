package com.yurakim.readingtrace.ai.service;

import com.yurakim.readingtrace.ai.dto.ChatResponseDto;
import com.yurakim.readingtrace.ai.dto.UserMessageDto;

public interface AiService {

    ChatResponseDto getResponseFromChatModel(UserMessageDto dto, String model);

}
