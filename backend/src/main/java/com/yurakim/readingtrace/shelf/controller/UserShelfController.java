package com.yurakim.readingtrace.shelf.controller;

import com.yurakim.readingtrace.shared.constant.ApiPath;
import com.yurakim.readingtrace.shelf.dto.CreateShelfRequestDto;
import com.yurakim.readingtrace.shelf.dto.CustomShelfDto;
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

}
