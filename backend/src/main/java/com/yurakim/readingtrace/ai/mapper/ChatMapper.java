package com.yurakim.readingtrace.ai.mapper;

import com.yurakim.readingtrace.ai.dto.ChatResponseDto;
import com.yurakim.readingtrace.ai.entity.ChatRecord;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {

    public ChatResponseDto toChatResponseDto(ChatRecord chatRecord){
        ChatResponseDto crDto = new ChatResponseDto();

        crDto.setChatRecordId(chatRecord.getId());
        crDto.setModel(chatRecord.getModel());
        crDto.setTimestamp(chatRecord.getTimestamp());

        crDto.setUserMessage(chatRecord.getUserMessage());
        crDto.setAssistantMessage(chatRecord.getAssistantMessage());

        crDto.setPromptTokens(chatRecord.getPromptTokens());
        crDto.setCompletionTokens(chatRecord.getCompletionTokens());
        crDto.setTotalTokens(chatRecord.getTotalTokens());

        crDto.setFinishReason(chatRecord.getFinishReason());
        crDto.setError(chatRecord.getError());

        return crDto;

    }

}
