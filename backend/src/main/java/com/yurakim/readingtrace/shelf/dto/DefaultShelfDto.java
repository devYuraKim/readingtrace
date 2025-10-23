package com.yurakim.readingtrace.shelf.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class DefaultShelfDto {
    private Long userId;
    private String name;
    private String slug;
    private Long bookCount;
    private Integer orderIndex;
}