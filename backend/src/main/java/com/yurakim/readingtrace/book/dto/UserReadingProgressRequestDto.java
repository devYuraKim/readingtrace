package com.yurakim.readingtrace.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReadingProgressRequestDto {

    private Long userId;
    private Long bookId;
    private Long userReadingStatusId;
    private Integer currentPage;

}
