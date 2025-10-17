package com.yurakim.readingtrace.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleBookDto {

    //TODO: check if I should add fields to match BookDto
    //TODO: check if changing the field name does not affect anything else >> seems ok
    private String externalId;
    private String title;
    private String authors;
    private String imageLinks;
    private String publisher;
    private String publishedDate;
    private String description;
    private String isbn10;
    private String isbn13;

}
