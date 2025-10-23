package com.yurakim.readingtrace.shelf.mapper;

import com.yurakim.readingtrace.shelf.dto.CustomShelfDto;
import com.yurakim.readingtrace.shelf.entity.Shelf;
import org.springframework.stereotype.Component;

@Component
public class ShelfMapper {

    public CustomShelfDto mapToDto(Shelf shelf){
        CustomShelfDto shelfDto = new CustomShelfDto();
        shelfDto.setShelfId(shelf.getId());
        shelfDto.setUserId(shelf.getUserId());
        shelfDto.setName(shelf.getName());
        shelfDto.setSlug(shelf.getSlug());
        shelfDto.setDescription(shelf.getDescription());
        shelfDto.setBookCount(shelf.getBookCount());
        shelfDto.setIsHidden(shelf.getIsHidden());
        shelfDto.setOrderIndex(shelf.getOrderIndex());
        return shelfDto;
    }





}
