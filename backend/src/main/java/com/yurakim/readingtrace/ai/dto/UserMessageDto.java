package com.yurakim.readingtrace.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class UserMessageDto {

    private Long userId;
    private String userMessage;
    private String title;
    private String author;
    private String publisher;
    private String publishedDate;
    private String isbn10;
    private String isbn13;
    private String language;
}