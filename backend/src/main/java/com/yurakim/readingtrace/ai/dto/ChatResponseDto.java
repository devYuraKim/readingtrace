package com.yurakim.readingtrace.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ChatResponseDto {

    private Date timestamp;
    private String textContent;

    private Integer promptTokens;
    private Integer completionTokens;
    private Integer totalTokens;

    private String messageType;
    private String finishReason;

}
