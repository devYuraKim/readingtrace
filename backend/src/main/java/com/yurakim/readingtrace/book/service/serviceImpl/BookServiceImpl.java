package com.yurakim.readingtrace.book.service.serviceImpl;

import com.yurakim.readingtrace.book.dto.UserBookDto;
import com.yurakim.readingtrace.book.entity.UserBook;
import com.yurakim.readingtrace.book.repository.UserBookRepository;
import com.yurakim.readingtrace.book.service.BookService;
import com.yurakim.readingtrace.book.spec.UserBookSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<UserBookDto> getUserBooks(Long userId, String status, String visibility, Integer rating) {
        Specification<UserBook> spec = UserBookSpecs.hasUserId(userId);

        if (status != null) spec = spec.and(UserBookSpecs.hasStatus(status));
        if (visibility != null) spec = spec.and(UserBookSpecs.hasVisibility(visibility));
        if (rating != null) spec = spec.and(UserBookSpecs.hasRating(rating));

        //TODO: introduce mapper to focus on querying
        return userBookRepository.findAll(spec).stream()
                .map( userBook -> {
                    UserBookDto userBookDto = new UserBookDto();
                    userBookDto.setUserId(userBook.getId());
                    userBookDto.setBookId(userBook.getBookId());
                    userBookDto.setStatus(userBook.getStatus());
                    userBookDto.setVisibility(userBook.getVisibility());
                    userBookDto.setRating(userBook.getRating());
                    return userBookDto;
                })
                .toList();
    }

}
