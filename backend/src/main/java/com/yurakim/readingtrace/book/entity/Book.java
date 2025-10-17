package com.yurakim.readingtrace.book.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String externalId; //GoogleBooksId

    @Column(nullable = false)
    private String title;

    private String authors;

    private String imageURL;

    private String publisher;

    private String publishedDate;

    private String description;

    private String isbn10;

    private String isbn13;

    private Integer pageCount;

    private List<String> categories;

    private Double averageRating;

    private Long ratingsCount;

    //TODO: Dimensions, AccessInfo >> include these?

}
