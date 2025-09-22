package com.yurakim.readingtrace.book.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserBookDto {
    private Long userId;
    private String bookId;
    private String status;
    private String visibility;
    private Integer rating;
    private Date startDate;
    private Date endDate;
}