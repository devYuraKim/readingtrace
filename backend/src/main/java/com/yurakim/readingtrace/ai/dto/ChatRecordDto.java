package com.yurakim.readingtrace.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRecordDto {

    private Long chatRecordId;
    private Long userId;
    private Long bookId;

    private String chatModel;
    private Date timestamp;

    private String userMessage;
    private String assistantMessage;

    private Integer promptTokens;
    private Integer completionTokens;
    private Integer totalTokens;
    private String finishReason;

    private Boolean isSuccess;
    private String error;

    private Boolean isBookmarked;
    private Boolean isDeleted;

//    private String chatVisibility;

}
