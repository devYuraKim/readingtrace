package com.yurakim.readingtrace.book.service.serviceImpl;

import com.yurakim.readingtrace.book.dto.UserBookStatusDto;
import com.yurakim.readingtrace.book.entity.UserBookStatus;
import com.yurakim.readingtrace.book.repository.UserBookStatusRepository;
import com.yurakim.readingtrace.book.service.UserBookStatusService;
import com.yurakim.readingtrace.book.spec.UserBookStatusSpecs;
import com.yurakim.readingtrace.shelf.entity.Shelf;
import com.yurakim.readingtrace.shelf.repository.ShelfRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBookStatusServiceImpl implements UserBookStatusService {

    private final ShelfRepository shelfRepository;
    private final UserBookStatusRepository userBookStatusRepository;

    //TODO: mapper

    @Override
    @Transactional
    public void createUserBookStatus(UserBookStatusDto userBookStatusDto) {
        UserBookStatus userBookStatus = new UserBookStatus();
        userBookStatus.setUserId(userBookStatusDto.getUserId());
        userBookStatus.setBookId(userBookStatusDto.getBookId());
        userBookStatus.setStatus(userBookStatusDto.getStatus());
        userBookStatus.setVisibility(userBookStatusDto.getVisibility());
        userBookStatus.setRating(userBookStatusDto.getRating());
        if(userBookStatusDto.getShelfId() == null) {
            String shelfSlug = userBookStatusDto.getStatus();
            Long userId = userBookStatusDto.getUserId();
            Shelf shelf = shelfRepository.findByUserIdAndSlug(userId, shelfSlug);
            userBookStatus.setShelfId(shelf.getId());
        } else{
            userBookStatus.setShelfId(userBookStatusDto.getShelfId());
        }
        userBookStatus.setStartDate(userBookStatusDto.getStartDate());
        userBookStatus.setEndDate(userBookStatusDto.getEndDate());
        userBookStatusRepository.save(userBookStatus);
    }

    @Override
    public UserBookStatusDto getUserBookStatus(Long userId, Long bookId) {
        UserBookStatus userBook = userBookStatusRepository.findByUserIdAndBookId(userId, bookId);

        //record not found if user hasn't added the book yet
        if(userBook == null){
            return new UserBookStatusDto();
        }

        UserBookStatusDto userBookStatusDto = new UserBookStatusDto();
        userBookStatusDto.setUserId(userBook.getUserId());
        userBookStatusDto.setBookId(userBook.getBookId());
        userBookStatusDto.setStatus(userBook.getStatus());
        userBookStatusDto.setVisibility(userBook.getVisibility());
        userBookStatusDto.setShelfId(userBook.getShelfId());
        userBookStatusDto.setRating(userBook.getRating());
        userBookStatusDto.setStartDate(userBook.getStartDate());
        userBookStatusDto.setEndDate(userBook.getEndDate());
        return userBookStatusDto;
    }

    @Override
    public UserBookStatusDto updateUserBookStatus(UserBookStatusDto userBookStatusDto){
        Long userId = userBookStatusDto.getUserId();
        Long bookId = userBookStatusDto.getBookId();
        UserBookStatus userBookStatus = userBookStatusRepository.findByUserIdAndBookId(userId, bookId);

        userBookStatus.setStatus(userBookStatusDto.getStatus());
        userBookStatus.setVisibility(userBookStatusDto.getVisibility());
        userBookStatus.setRating(userBookStatusDto.getRating());
        if(userBookStatusDto.getShelfId() == null) {
            String shelfSlug = userBookStatusDto.getStatus();
            Shelf shelf = shelfRepository.findByUserIdAndSlug(userId, shelfSlug);
            userBookStatus.setShelfId(shelf.getId());
        } else{
            userBookStatus.setShelfId(userBookStatusDto.getShelfId());
        }        userBookStatus.setStartDate(userBookStatusDto.getStartDate());
        userBookStatus.setEndDate(userBookStatusDto.getEndDate());
        UserBookStatus updatedUserBook = userBookStatusRepository.save(userBookStatus);

        UserBookStatusDto resultDto = new UserBookStatusDto();
        resultDto.setUserId(updatedUserBook.getUserId());
        resultDto.setBookId(updatedUserBook.getBookId());
        resultDto.setStatus(updatedUserBook.getStatus());
        resultDto.setVisibility(updatedUserBook.getVisibility());
        resultDto.setShelfId(updatedUserBook.getShelfId());
        resultDto.setRating(updatedUserBook.getRating());
        resultDto.setStartDate(updatedUserBook.getStartDate());
        resultDto.setEndDate(updatedUserBook.getEndDate());

        return userBookStatusDto;
    }

    @Override
    public void deleteUserBookStatus(Long userId, Long bookId) {
        UserBookStatus userBook = userBookStatusRepository.findByUserIdAndBookId(userId, bookId);
        if(userBook != null) userBookStatusRepository.delete(userBook);
    }

    @Override
    public List<UserBookStatusDto> getUserBooksStatus(Long userId, Long shelfId, String status, String visibility, Integer rating) {
        Specification<UserBookStatus> spec = UserBookStatusSpecs.hasUserId(userId);

        if (shelfId != null) spec = spec.and(UserBookStatusSpecs.hasShelfId(shelfId));
        if (status != null) spec = spec.and(UserBookStatusSpecs.hasStatus(status));
        if (visibility != null) spec = spec.and(UserBookStatusSpecs.hasVisibility(visibility));
        if (rating != null) spec = spec.and(UserBookStatusSpecs.hasRating(rating));

        //TODO: introduce mapper to focus on querying
        return userBookStatusRepository.findAll(spec).stream()
                .map( userBook -> {
                    UserBookStatusDto userBookStatusDto = new UserBookStatusDto();
                    userBookStatusDto.setUserId(userBook.getId());
                    userBookStatusDto.setBookId(userBook.getBookId());
                    userBookStatusDto.setStatus(userBook.getStatus());
                    userBookStatusDto.setVisibility(userBook.getVisibility());
                    userBookStatusDto.setShelfId(userBook.getShelfId());
                    userBookStatusDto.setRating(userBook.getRating());
                    return userBookStatusDto;
                })
                .toList();
    }
}
