package com.yurakim.readingtrace.ai.mapper;

import com.yurakim.readingtrace.ai.dto.ChatResponseDto;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {

    public ChatResponseDto toChatResponseDto(ChatResponse chatResponse){
        ChatResponseDto crDto = new ChatResponseDto();

        //TIMESTAMP is set based on UserMessageDto
        crDto.setTextContent(chatResponse.getResult().getOutput().getText());

        crDto.setPromptTokens(chatResponse.getMetadata().getUsage().getPromptTokens());
        crDto.setCompletionTokens(chatResponse.getMetadata().getUsage().getCompletionTokens());
        crDto.setTotalTokens(chatResponse.getMetadata().getUsage().getTotalTokens());

        crDto.setMessageType(chatResponse.getResult().getOutput().getMessageType().getValue());
        crDto.setFinishReason(chatResponse.getResult().getMetadata().getFinishReason());

        return crDto;

    }

}
