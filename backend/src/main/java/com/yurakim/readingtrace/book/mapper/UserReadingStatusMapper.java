package com.yurakim.readingtrace.book.mapper;

import com.yurakim.readingtrace.book.dto.UserReadingStatusDto;
import com.yurakim.readingtrace.book.entity.UserReadingStatus;
import org.springframework.stereotype.Component;

@Component
public class UserReadingStatusMapper {

    public UserReadingStatus mapToEntity(UserReadingStatusDto ursDto) {
        UserReadingStatus urs = new UserReadingStatus();
        urs.setUserId(ursDto.getUserId());
        urs.setBookId(ursDto.getBookId());
        urs.setStatus(ursDto.getStatus());
        urs.setVisibility(ursDto.getVisibility());
        urs.setRating(ursDto.getRating());
        urs.setShelfId(ursDto.getShelfId());
        urs.setStartDate(ursDto.getStartDate());
        urs.setEndDate(ursDto.getEndDate());
        return urs;
    }

    public UserReadingStatus mapToEntity(UserReadingStatusDto ursDto, UserReadingStatus urs) {
        urs.setUserId(ursDto.getUserId());
        urs.setBookId(ursDto.getBookId());
        urs.setStatus(ursDto.getStatus());
        urs.setVisibility(ursDto.getVisibility());
        urs.setRating(ursDto.getRating());
        urs.setShelfId(ursDto.getShelfId());
        urs.setStartDate(ursDto.getStartDate());
        urs.setEndDate(ursDto.getEndDate());
        return urs;
    }

    public UserReadingStatusDto mapToDto(UserReadingStatus urs){
        UserReadingStatusDto ursDto = new UserReadingStatusDto();
        ursDto.setUserId(urs.getUserId());
        ursDto.setBookId(urs.getBookId());
        ursDto.setShelfId(urs.getShelfId());
        ursDto.setStatus(urs.getStatus());
        ursDto.setVisibility(urs.getVisibility());
        ursDto.setRating(urs.getRating());
        ursDto.setStartDate(urs.getStartDate());
        ursDto.setEndDate(urs.getEndDate());
        return ursDto;
    }

}
