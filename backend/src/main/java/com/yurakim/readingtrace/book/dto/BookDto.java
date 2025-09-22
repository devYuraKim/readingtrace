package com.yurakim.readingtrace.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private String bookId;
    private String title;
    private String authors;
    private String imageLinks;
    private String publisher;
    private String publishedDate;
    private String description;
    private String isbn10;
    private String isbn13;

}

//interface bookType {
//    id: string;
//    title: string;
//    authors: string;
//    imageLinks: string;
//    publisher: string;
//    publishedDate: string;
//    description: string;
//    isbn_10: string;
//    isbn_13: string;
//}
