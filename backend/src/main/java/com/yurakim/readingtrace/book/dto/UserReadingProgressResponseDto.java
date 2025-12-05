package com.yurakim.readingtrace.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReadingProgressResponseDto {

    private Long userReadingProgressId;
    private Long userId;
    private Long bookId;
    private Long userReadingStatusId;
    private Integer currentPage;
    private LocalDateTime userReadingProgressCreatedAt;

}
