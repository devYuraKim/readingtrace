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
        Shelf shelf;
        UserReadingStatus urs = userReadingStatusMapper.mapToEntity(ursDto);
        //case A. default shelves
        if(urs.getShelfId() == null) {
            String shelfSlug = urs.getStatus();
            Long userId = urs.getUserId();
            shelf = shelfRepository.findByUserIdAndSlug(userId, shelfSlug);
            //case B. custom shelves
        } else{
            Long shelfId = urs.getShelfId();
            shelf = shelfRepository.findById(shelfId).orElseThrow(() -> new RuntimeException(
                    String.format("FAILED TO CREATE: No shelf found for [ShelfId: %d]", shelfId)
            ));
        }
        //check if the book already exists in the shelf
        boolean exists = userReadingStatusRepository.existsByUserIdAndBookIdAndShelfId(
                urs.getUserId(), urs.getBookId(), shelf.getId()
        );
        if (exists) throw new RuntimeException(
                String.format("FAILED TO CREATE: Book with [BookId: %d] already exists in shelf [ShelfId: %d]", urs.getBookId(), urs.getShelfId())
        );
        //if not, add the book to the shelf
        shelf.setBookCount(shelf.getBookCount()+1);
        shelfRepository.save(shelf);
        urs.setShelfId(shelf.getId());
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
    public UserReadingStatusDto updateUserReadingStatus(UserReadingStatusDto newUrsDto){
        Long userId = newUrsDto.getUserId();
        Long bookId = newUrsDto.getBookId();
        UserReadingStatus existingUrsEntity = userReadingStatusRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(()-> new RuntimeException(String.format("FAILED TO UPDATE: UserReadingStatus for [UserId: %d] and [BookId: %d] not found", userId, bookId)));
        //overwrite updateUrsDto to existingUrsEntity
        UserReadingStatus mappedURS = userReadingStatusMapper.mapToEntity(newUrsDto, existingUrsEntity);

        Long newShelfId = newUrsDto.getShelfId();
        Long existingShelfId = existingUrsEntity.getShelfId();

        Shelf newShelf = shelfRepository.findById(newShelfId).orElseGet(null);
        Shelf existingShelf = shelfRepository.findById(existingShelfId).orElseGet(null);

        String isNewShelfDefault = newShelf.getIsDefault().toString();
        String isExistingShelfDefault = existingShelf.getIsDefault().toString();

        System.out.println(String.format("Existing shelf id %d, default %s",  existingShelfId, isExistingShelfDefault));
        System.out.println(String.format("New shelf: id %d, default %s", newShelfId, isNewShelfDefault));

//        if(mappedURS.getShelfId() == null) {
//            String shelfSlug = mappedURS.getStatus();
//            Shelf shelf = shelfRepository.findByUserIdAndSlug(userId, shelfSlug);
//            mappedURS.setShelfId(shelf.getId());
//        } else{
//            mappedURS.setShelfId(mappedURS.getShelfId());
//        }
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
    public List<UserReadingStatusDto> getUserReadingStatuses(Long userId, Long shelfId, String status, String visibility, Integer rating) {
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