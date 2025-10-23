package com.yurakim.readingtrace.shelf.controller;

import com.yurakim.readingtrace.shared.constant.ApiPath;
import com.yurakim.readingtrace.shelf.dto.CreateShelfRequestDto;
import com.yurakim.readingtrace.shelf.dto.ShelfDto;
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

    //  UserShelf
    @GetMapping("/custom")
    public ResponseEntity<List<ShelfDto>> getUserShelves(@PathVariable Long userId) {
        List<ShelfDto> resultList = shelfService.getUserShelves(userId);
        return ResponseEntity.ok(resultList);
    }

    @PostMapping
    public ResponseEntity<ShelfDto> createUserShelf(@PathVariable Long userId, @RequestBody CreateShelfRequestDto csrDto){
        ShelfDto resultShelf = shelfService.createUserShelf(userId, csrDto.getShelfName());
        return ResponseEntity.ok(resultShelf);
    }

}
