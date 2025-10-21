package com.yurakim.readingtrace.book.mapper;

import com.yurakim.readingtrace.book.dto.BookDto;
import com.yurakim.readingtrace.book.dto.UserBookDto;
import com.yurakim.readingtrace.book.dto.UserReadingStatusDto;
import org.springframework.stereotype.Component;

@Component
public class UserBookMapper {

    public BookDto extractBookDto(UserBookDto userBookDto){
        BookDto bookDto = new BookDto();
        //bookDto.bookId = null
        bookDto.setExternalId(userBookDto.getExternalId());
        bookDto.setTitle(userBookDto.getTitle());
        bookDto.setAuthors(userBookDto.getAuthors());
        bookDto.setImageLinks(userBookDto.getImageLinks());
        bookDto.setPublisher(userBookDto.getPublisher());
        bookDto.setPublishedDate(userBookDto.getPublishedDate());
        bookDto.setDescription(userBookDto.getDescription());
        bookDto.setIsbn10(userBookDto.getIsbn10());
        bookDto.setIsbn13(userBookDto.getIsbn13());
        bookDto.setPageCount(userBookDto.getPageCount());
        bookDto.setMainCategory(userBookDto.getMainCategory());
        bookDto.setCategories(userBookDto.getCategories());
        bookDto.setAverageRating(userBookDto.getAverageRating());
        bookDto.setRatingsCount(userBookDto.getRatingsCount());
        bookDto.setLanguage(userBookDto.getLanguage());
        return bookDto;
    }

    public UserReadingStatusDto extractUserReadingStatusDto(UserBookDto userBookDto) {
        UserReadingStatusDto ursDto =  new UserReadingStatusDto();
        ursDto.setUserReadingStatusId(userBookDto.getUserReadingStatusId());
        ursDto.setUserId(userBookDto.getUserId());
        ursDto.setShelfId(userBookDto.getShelfId());
        ursDto.setBookId(userBookDto.getBookId());
        ursDto.setStatus(userBookDto.getStatus());
        ursDto.setVisibility(userBookDto.getVisibility());
        ursDto.setRating(userBookDto.getRating());
        ursDto.setStartDate(userBookDto.getStartDate());
        ursDto.setEndDate(userBookDto.getEndDate());
        ursDto.setUserReadingStatusCreatedAt(ursDto.getUserReadingStatusCreatedAt());
        ursDto.setUserReadingStatusUpdatedAt(ursDto.getUserReadingStatusUpdatedAt());
        //TODO: check if metadata is needed
        //ursDto.setUserReadingStatusMetadata(ursDto.getUserReadingStatusMetadata());
        return ursDto;
    }

    public UserBookDto combineDTOs(BookDto bookDto, UserReadingStatusDto ursDto){

        UserBookDto userBookDto = new UserBookDto();
        //BookDto
        userBookDto.setBookId(bookDto.getBookId());
        userBookDto.setExternalId(bookDto.getExternalId());
        userBookDto.setTitle(bookDto.getTitle());
        userBookDto.setAuthors(bookDto.getAuthors());
        userBookDto.setImageLinks(bookDto.getImageLinks());
        userBookDto.setPublisher(bookDto.getPublisher());
        userBookDto.setPublishedDate(bookDto.getPublishedDate());
        userBookDto.setDescription(bookDto.getDescription());
        userBookDto.setIsbn10(bookDto.getIsbn10());
        userBookDto.setIsbn13(bookDto.getIsbn13());
        userBookDto.setPageCount(bookDto.getPageCount());
        userBookDto.setMainCategory(bookDto.getMainCategory());
        userBookDto.setCategories(bookDto.getCategories());
        userBookDto.setAverageRating(bookDto.getAverageRating());
        userBookDto.setRatingsCount(bookDto.getRatingsCount());
        userBookDto.setLanguage(bookDto.getLanguage());
        //UserReadingStatus
        userBookDto.setUserReadingStatusId(ursDto.getUserReadingStatusId());
        userBookDto.setUserId(ursDto.getUserId());
        userBookDto.setShelfId(ursDto.getShelfId());
        userBookDto.setStatus(ursDto.getStatus());
        userBookDto.setVisibility(ursDto.getVisibility());
        userBookDto.setRating(ursDto.getRating());
        userBookDto.setStartDate(ursDto.getStartDate());
        userBookDto.setEndDate(ursDto.getEndDate());
        userBookDto.setUserReadingStatusCreatedAt(ursDto.getUserReadingStatusCreatedAt());
        userBookDto.setUserReadingStatusUpdatedAt(ursDto.getUserReadingStatusUpdatedAt());

        return userBookDto;
    }
}
