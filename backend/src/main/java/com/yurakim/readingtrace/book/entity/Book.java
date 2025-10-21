package com.yurakim.readingtrace.book.entity;

import com.yurakim.readingtrace.book.util.JsonStringListConverter;
import com.yurakim.readingtrace.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class Book extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String externalId; //GoogleBooksId

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "json")
    @Convert(converter = JsonStringListConverter.class)
    private List<String> authors;

    private String imageURL;

    private String publisher;

    private String publishedDate;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    private String isbn10;

    private String isbn13;

    private Integer pageCount;

    private String mainCategory;

    @Column(columnDefinition = "json")
    @Convert(converter = JsonStringListConverter.class)
    private List<String> categories;

    private Double averageRating;

    private Long ratingsCount;

    private String language;

    @Column(columnDefinition = "json")
    private String metadata;

    //TODO: Dimensions, AccessInfo >> include these?
}
