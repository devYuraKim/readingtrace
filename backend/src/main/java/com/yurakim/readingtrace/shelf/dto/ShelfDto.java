package com.yurakim.readingtrace.shelf.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ShelfDto {
    private Long shelfId;
    private Long userId;
    private String name;
    private String slug;
    private String description;
    private Integer bookCount;
    private Boolean isDefault;
    private Long defaultShelfId;
    private Boolean isHidden;
    private Integer orderIndex;

}