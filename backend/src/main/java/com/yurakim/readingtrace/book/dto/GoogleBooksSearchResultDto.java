package com.yurakim.readingtrace.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class GoogleBooksSearchResultDto {
    private List<BookDto> books;
    private int totalItems;
}
