package com.yurakim.readingtrace.book.service;

import com.yurakim.readingtrace.book.dto.UserBookDto;

import java.util.List;

public interface BookService {

    void addUserBook(UserBookDto userBookDto);

    List<UserBookDto> getUserBooks(Long userId, String status, String visibility, Integer rating);
}
