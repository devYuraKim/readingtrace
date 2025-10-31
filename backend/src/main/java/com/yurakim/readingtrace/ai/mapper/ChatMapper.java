package com.yurakim.readingtrace.ai.mapper;

import com.yurakim.readingtrace.ai.dto.ChatRecordDto;
import com.yurakim.readingtrace.ai.entity.ChatRecord;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {

    public ChatRecordDto toChatRecordDto(ChatRecord chatRecord){
        ChatRecordDto crDto = new ChatRecordDto();

        crDto.setChatRecordId(chatRecord.getId());
        crDto.setUserId(chatRecord.getUserId());
        crDto.setBookId(chatRecord.getBookId());

        crDto.setTimestamp(chatRecord.getTimestamp());
        crDto.setChatModel(chatRecord.getModel());
        crDto.setUserMessage(chatRecord.getUserMessage());
        crDto.setAssistantMessage(chatRecord.getAssistantMessage());

        crDto.setPromptTokens(chatRecord.getPromptTokens());
        crDto.setCompletionTokens(chatRecord.getCompletionTokens());
        crDto.setTotalTokens(chatRecord.getTotalTokens());

        crDto.setFinishReason(chatRecord.getFinishReason());
        crDto.setIsSuccess(chatRecord.getIsSuccess());
        crDto.setError(chatRecord.getError());

        return crDto;

//        ChatRecordDto
//        private String visibility;

    }

}
