package com.yurakim.readingtrace.book.mapper;

import com.yurakim.readingtrace.book.dto.BookDto;
import com.yurakim.readingtrace.book.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookDto mapToDto (Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setBookId(book.getId());
        bookDto.setExternalId(book.getExternalId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthors(book.getAuthors());
        bookDto.setImageLinks(book.getImageURL());
        bookDto.setPublisher(book.getPublisher());
        bookDto.setPublishedDate(book.getPublishedDate());
        bookDto.setDescription(book.getDescription());
        bookDto.setIsbn10(book.getIsbn10());
        bookDto.setIsbn13(book.getIsbn13());
        bookDto.setPageCount(book.getPageCount());
        bookDto.setMainCategory(book.getMainCategory());
        bookDto.setCategories(book.getCategories());
        bookDto.setAverageRating(book.getAverageRating());
        bookDto.setRatingsCount(book.getRatingsCount());
        bookDto.setLanguage(book.getLanguage());
        return bookDto;
    }

    public Book mapToEntity (BookDto bookDto) {
        Book book = new Book();
        book.setId(bookDto.getBookId());
        book.setExternalId(bookDto.getExternalId());
        book.setTitle(bookDto.getTitle());
        book.setAuthors(bookDto.getAuthors());
        book.setImageURL(bookDto.getImageLinks());
        book.setPublisher(bookDto.getPublisher());
        book.setPublishedDate(bookDto.getPublishedDate());
        book.setDescription(bookDto.getDescription());
        book.setIsbn10(bookDto.getIsbn10());
        book.setIsbn13(bookDto.getIsbn13());
        book.setPageCount(bookDto.getPageCount());
        book.setMainCategory(bookDto.getMainCategory());
        book.setCategories(bookDto.getCategories());
        book.setAverageRating(bookDto.getAverageRating());
        book.setRatingsCount(bookDto.getRatingsCount());
        book.setLanguage(bookDto.getLanguage());
        return book;
    }

}
