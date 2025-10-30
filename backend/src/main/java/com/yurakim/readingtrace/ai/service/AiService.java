package com.yurakim.readingtrace.ai.service;

import com.yurakim.readingtrace.ai.dto.UserMessageDto;

public interface AiService {

    String getResponseFromChatModel(UserMessageDto dto, String model);

}
