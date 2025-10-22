package com.yurakim.readingtrace.book.service.serviceImpl;

import com.yurakim.readingtrace.book.dto.UserReadingStatusDto;
import com.yurakim.readingtrace.book.entity.UserReadingStatus;
import com.yurakim.readingtrace.book.mapper.UserReadingStatusMapper;
import com.yurakim.readingtrace.book.repository.UserReadingStatusRepository;
import com.yurakim.readingtrace.book.service.UserReadingStatusService;
import com.yurakim.readingtrace.book.spec.UserReadingStatusSpecs;
import com.yurakim.readingtrace.shelf.entity.Shelf;
import com.yurakim.readingtrace.shelf.repository.ShelfRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserReadingStatusServiceImpl implements UserReadingStatusService {

    private final ShelfRepository shelfRepository;
    private final UserReadingStatusRepository userReadingStatusRepository;
    private final UserReadingStatusMapper userReadingStatusMapper;

    @Override
    @Transactional
    public void createUserReadingStatus(UserReadingStatusDto ursDto) {
        UserReadingStatus urs = userReadingStatusMapper.mapToEntity(ursDto);
        if(urs.getShelfId() == null) {
            String shelfSlug = urs.getStatus();
            Long userId = urs.getUserId();
            Shelf shelf = shelfRepository.findByUserIdAndSlug(userId, shelfSlug);
            urs.setShelfId(shelf.getId());
        } else{
            urs.setShelfId(urs.getShelfId());
        }
        userReadingStatusRepository.save(urs);
    }

    @Override
    public UserReadingStatusDto getUserReadingStatus(Long userId, Long bookId) {
        //Do not throw error when foundURS is null, instead return an empty URSDto
        UserReadingStatus foundURS = userReadingStatusRepository.findByUserIdAndBookId(userId, bookId)
                .orElseGet(()-> new UserReadingStatus());
        UserReadingStatusDto ursDto = userReadingStatusMapper.mapToDto(foundURS);
        return ursDto;
    }

    @Override
    public UserReadingStatusDto updateUserReadingStatus(UserReadingStatusDto ursDto){
        Long userId = ursDto.getUserId();
        Long bookId = ursDto.getBookId();
        UserReadingStatus foundURS = userReadingStatusRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(()-> new RuntimeException(String.format("FAILED TO UPDATE: UserReadingStatus for [UserId: %d] and [BookId: %d] not found", userId, bookId)));
        UserReadingStatus mappedURS = userReadingStatusMapper.mapToEntity(ursDto, foundURS);
        if(mappedURS.getShelfId() == null) {
            String shelfSlug = mappedURS.getStatus();
            Shelf shelf = shelfRepository.findByUserIdAndSlug(userId, shelfSlug);
            mappedURS.setShelfId(shelf.getId());
        } else{
            mappedURS.setShelfId(mappedURS.getShelfId());
        }
        UserReadingStatus updatedURS = userReadingStatusRepository.save(mappedURS);
        UserReadingStatusDto mappedURSDto = userReadingStatusMapper.mapToDto(updatedURS);
        return mappedURSDto;
    }

    @Override
    public void deleteUserReadingStatus(Long userId, Long bookId) {
        UserReadingStatus foundURS = userReadingStatusRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(()-> new RuntimeException(String.format("FAILED TO DELETE: UserReadingStatus for [UserId: %d] and [BookId: %d] not found", userId, bookId)));
        if(foundURS != null) userReadingStatusRepository.delete(foundURS);
    }

    @Override
    public List<UserReadingStatusDto> getUserBooksStatus(Long userId, Long shelfId, String status, String visibility, Integer rating) {
        Specification<UserReadingStatus> spec = UserReadingStatusSpecs.hasUserId(userId);

        if (shelfId != null) spec = spec.and(UserReadingStatusSpecs.hasShelfId(shelfId));
        if (status != null) spec = spec.and(UserReadingStatusSpecs.hasStatus(status));
        if (visibility != null) spec = spec.and(UserReadingStatusSpecs.hasVisibility(visibility));
        if (rating != null) spec = spec.and(UserReadingStatusSpecs.hasRating(rating));

        return userReadingStatusRepository.findAll(spec).stream()
                .map( urs -> {
                    UserReadingStatusDto ursDto = userReadingStatusMapper.mapToDto(urs);
                    return ursDto;
                })
                .toList();
    }
}
