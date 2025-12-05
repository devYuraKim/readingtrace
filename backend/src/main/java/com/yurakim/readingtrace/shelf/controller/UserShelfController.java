package com.yurakim.readingtrace.shelf.controller;

import com.yurakim.readingtrace.book.repository.BookRepository;
import com.yurakim.readingtrace.book.repository.UserReadingStatusRepository;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import com.yurakim.readingtrace.shelf.dto.CreateShelfRequestDto;
import com.yurakim.readingtrace.shelf.dto.CustomShelfDto;
import com.yurakim.readingtrace.shelf.dto.DefaultShelfDto;
import com.yurakim.readingtrace.shelf.enums.DefaultShelfType;
import com.yurakim.readingtrace.shelf.service.ShelfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPath.USERSHELF) // /api/v1/users/{userId}/shelves
public class UserShelfController {

    private final ShelfService shelfService;
    private final UserReadingStatusRepository userReadingStatusRepository;
    private final BookRepository bookRepository;

    // CustomShelf
    @GetMapping("/custom")
    public ResponseEntity<List<CustomShelfDto>> getCustomShelves(@PathVariable Long userId) {
        List<CustomShelfDto> resultList = shelfService.getCustomShelves(userId);
        return ResponseEntity.ok(resultList);
    }

    @PostMapping
    public ResponseEntity<CustomShelfDto> createUserShelf(@PathVariable Long userId, @RequestBody CreateShelfRequestDto csrDto){
        CustomShelfDto resultShelf = shelfService.createCustomShelf(userId, csrDto.getShelfName());
        return ResponseEntity.ok(resultShelf);
    }

    // DefaultShelf
    @GetMapping("/default")
    public ResponseEntity<List<DefaultShelfDto>> getDefaultShelves(@PathVariable Long userId){

        List<DefaultShelfDto> defaultShelfDtoList = List.of(DefaultShelfType.values()).stream().map((ds) -> {

            Long bookCount;

            DefaultShelfDto dsDto = new DefaultShelfDto();
            dsDto.setUserId(userId);
            dsDto.setName(ds.getName());
            dsDto.setSlug(ds.getSlug());
            dsDto.setOrderIndex(Integer.valueOf(ds.getOrderIndex()));
            if(ds.getSlug().toLowerCase().equals("all")) {
                bookCount = userReadingStatusRepository.countAllByUserId(userId);
            } else {
                bookCount = userReadingStatusRepository.countAllByUserIdAndStatus(userId, ds.getSlug());
            }
            dsDto.setBookCount(bookCount);
            return dsDto;
        }).toList();

        return ResponseEntity.ok(defaultShelfDtoList);
    }

}
