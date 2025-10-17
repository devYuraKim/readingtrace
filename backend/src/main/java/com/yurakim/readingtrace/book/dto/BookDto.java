package com.yurakim.readingtrace.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private Long bookId;
    private String externalId;
    private String title;
    private String subtitle;
    private String authors;
    private String imageLinks;
    private String publisher;
    private String publishedDate;
    private String description;
    private String isbn10;
    private String isbn13;
    private Integer pageCount;
    private String printType;
    private String mainCategory;
    private List<String> categories;
    private Double averageRating;
    private Long ratingsCount;
    private String language;
    private String infoLink;

    //TODO: ImageLinks, Dimensions, AccessInfo >> how to handle nested fields
    //TODO: authors, isbn >> check how I handled these fields
}
