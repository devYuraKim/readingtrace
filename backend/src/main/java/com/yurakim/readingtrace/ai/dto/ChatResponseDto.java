package com.yurakim.readingtrace.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ChatResponseDto {

    private Long chatRecordId;
    private String model;
    private Date timestamp;

    private String userMessage;
    private String assistantMessage;

    private Integer promptTokens;
    private Integer completionTokens;
    private Integer totalTokens;

    private String finishReason;
    private String error;

}