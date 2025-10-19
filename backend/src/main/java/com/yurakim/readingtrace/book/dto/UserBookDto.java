package com.yurakim.readingtrace.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserBookDto {

    //Book
    private Long bookId;
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
    //UserBookStatus
    private Long userBookStatusId;
    private Long userId;
    private Long shelfId;
    private String status;
    private String visibility;
    private Integer rating;
    private Date startDate;
    private Date endDate;
    private String userBookStatusMetadata;
    private LocalDateTime userBookStatusCreatedAt;
    private LocalDateTime userBookStatusUpdatedAt;

}
