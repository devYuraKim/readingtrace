package com.yurakim.readingtrace.note.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserNoteDto {
    private Long noteId;
    private Long userId;
    private Long bookId;
    private String referencePage;
    private Integer referenceLine;
    private String noteContent;
    private String noteTitle;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}