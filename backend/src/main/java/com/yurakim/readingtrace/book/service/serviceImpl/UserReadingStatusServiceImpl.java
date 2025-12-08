package com.yurakim.readingtrace.book.service.serviceImpl;

import com.yurakim.readingtrace.ai.service.AiService;
import com.yurakim.readingtrace.book.dto.UserReadingStatusDto;
import com.yurakim.readingtrace.book.entity.Book;
import com.yurakim.readingtrace.book.entity.UserReadingStatus;
import com.yurakim.readingtrace.book.mapper.UserReadingStatusMapper;
import com.yurakim.readingtrace.book.repository.BookRepository;
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
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserReadingStatusServiceImpl implements UserReadingStatusService {

    private final ShelfRepository shelfRepository;
    private final UserReadingStatusRepository userReadingStatusRepository;
    private final UserReadingStatusMapper userReadingStatusMapper;
    private final AiService aiService;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public void createUserReadingStatus(UserReadingStatusDto ursDto) {
        if(null == ursDto.getStatus()) throw new RuntimeException("FAILED TO CREATE: status cannot be null");

        UserReadingStatus urs = new UserReadingStatus();
        urs.setId(ursDto.getUserReadingStatusId());
        urs.setUserId(ursDto.getUserId());
        urs.setShelfId(ursDto.getShelfId());
        Book book = bookRepository.findById(ursDto.getBookDto().getBookId()).orElseThrow(()->new RuntimeException("No book found with id: " + ursDto.getBookDto().getBookId()));
        urs.setBook(book);
        urs.setStatus(ursDto.getStatus());
        urs.setVisibility(ursDto.getVisibility());
        urs.setRating(ursDto.getRating());
        urs.setStartDate(ursDto.getStartDate());
        urs.setEndDate(ursDto.getEndDate());

        //check if shelfId exists or not
        Long shelfId = urs.getShelfId();
        if(null != shelfId){
            //check if the shelf exists by the shelfId
            Shelf shelf = shelfRepository.findById(shelfId).orElseThrow(() -> new RuntimeException(
                    String.format("FAILED TO CREATE: No shelf found for [ShelfId: %d]", shelfId)
            ));
            //check if the book already exists in the shelf
            boolean exists = userReadingStatusRepository.existsByUserIdAndBookIdAndShelfId(
                    urs.getUserId(), urs.getBook().getId(), shelf.getId()
            );
            if (exists) throw new RuntimeException(
                    String.format("FAILED TO CREATE: Book with [BookId: %d] already exists in shelf [ShelfId: %d]", urs.getBook().getId(), urs.getShelfId())
            );
            //if all checks cleared, increase bookCount by 1 and save in the shelf
            shelf.setBookCount(shelf.getBookCount()+1);
            shelfRepository.save(shelf);
            urs.setShelfId(shelf.getId());
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

    public UserReadingStatus getUserReadingStatus(Long userReadingStatusId){
        UserReadingStatus userReadingStatus = userReadingStatusRepository.findById(userReadingStatusId).orElseThrow(()-> new RuntimeException(String.format("No UserReadingStatus for [UserReadingStatusId]: %d", userReadingStatusId)));
        return userReadingStatus;
    }

    @Override
    @Transactional
    public UserReadingStatusDto updateUserReadingStatus(UserReadingStatusDto ursDto){
        Long userId = ursDto.getUserId();
        Long bookId = ursDto.getBookDto().getBookId();
        //check if original record exists
        UserReadingStatus existingUrs = userReadingStatusRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(()-> new RuntimeException(String.format("FAILED TO UPDATE: UserReadingStatus for [UserId: %d] and [BookId: %d] not found", userId, bookId)));
        System.out.println("existing URS: " + existingUrs);
        Long existingShelfId = existingUrs.getShelfId();
        //overwrite updateUrsDto to existingUrsEntity
        UserReadingStatus updatedUrs = userReadingStatusMapper.mapToEntity(ursDto, existingUrs);
        System.out.println("new URS: " + updatedUrs);
        Long updatedShelfId = updatedUrs.getShelfId();
        //if existing and updated shelfIds differ
        if (!Objects.equals(existingShelfId, updatedShelfId)){
            //if existing shelf is not 'default(logical group)', decrement bookCount of that shelf
            if(null != existingShelfId){
                Shelf existingShelf = shelfRepository.findById(existingShelfId)
                    .orElseThrow(() -> new RuntimeException(String.format("FAILED TO UPDATE: No existing shelf found with [ShelfId: %d]", existingShelfId)));
                existingShelf.setBookCount(existingShelf.getBookCount()-1);
                shelfRepository.save(existingShelf);
            }
            //if updated shelf is not 'default(logical group)', increment bookCount of that shelf
            if(null != updatedShelfId){
            Shelf updatedShelf = shelfRepository.findById(updatedShelfId)
                    .orElseThrow(() -> new RuntimeException(String.format("FAILED TO UPDATE: No shelf found with [ShelfId: %d]", updatedShelfId)));
            updatedShelf.setBookCount(updatedShelf.getBookCount()+1);
            shelfRepository.save(updatedShelf);
            }
        }
        UserReadingStatus resultUrs = userReadingStatusRepository.save(updatedUrs);
        UserReadingStatusDto resultUrsDto = userReadingStatusMapper.mapToDto(resultUrs);
        return resultUrsDto;
    }

    @Override
    public void deleteUserReadingStatus(Long userId, Long bookId) {
        UserReadingStatus existingUrs = userReadingStatusRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(()-> new RuntimeException(String.format("FAILED TO DELETE: UserReadingStatus for [UserId: %d] and [BookId: %d] not found", userId, bookId)));
        Long existingShelfId =  existingUrs.getShelfId();
        if(null != existingShelfId){
            Shelf existingShelf = shelfRepository.findById(existingShelfId)
                    .orElseThrow(() -> new RuntimeException(String.format("FAILED TO DELETE: No existing shelf found with [ShelfId: %d]", existingShelfId)));
            existingShelf.setBookCount(existingShelf.getBookCount()-1);
            shelfRepository.save(existingShelf);
        }
        //TODO: also delete notes and chats
        aiService.softDeleteChatRecords(userId, bookId);

        userReadingStatusRepository.delete(existingUrs);
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