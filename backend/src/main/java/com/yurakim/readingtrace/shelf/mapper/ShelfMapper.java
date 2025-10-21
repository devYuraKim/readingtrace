package com.yurakim.readingtrace.shelf.mapper;

import com.yurakim.readingtrace.shelf.dto.ShelfDto;
import com.yurakim.readingtrace.shelf.entity.Shelf;
import org.springframework.stereotype.Component;

@Component
public class ShelfMapper {

    public ShelfDto mapToDto(Shelf shelf){
        ShelfDto shelfDto = new ShelfDto();
        shelfDto.setShelfId(shelf.getId());
        shelfDto.setUserId(shelf.getUserId());
        shelfDto.setName(shelf.getName());
        shelfDto.setSlug(shelf.getSlug());
        shelfDto.setDescription(shelf.getDescription());
        shelfDto.setBookCount(shelf.getBookCount());
        shelfDto.setIsDefault(shelf.getIsDefault());
        return shelfDto;
    }





}
