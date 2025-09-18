package com.yurakim.readingtrace.book.service.serviceImpl;

import com.yurakim.readingtrace.book.dto.UserBookDto;
import com.yurakim.readingtrace.book.entity.UserBook;
import com.yurakim.readingtrace.book.repository.UserBookRepository;
import com.yurakim.readingtrace.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final UserBookRepository userBookRepository;

    @Override
    public void addUserBook(UserBookDto userBookDto) {
        UserBook userBook = new UserBook();
        userBook.setUserId(userBookDto.getUserId());
        userBook.setBookId(userBookDto.getBookId());
        userBook.setStatus(userBookDto.getStatus());
        userBook.setVisibility(userBookDto.getVisibility());
        userBook.setRating(userBookDto.getRating());
        userBookRepository.save(userBook);
    }

}
