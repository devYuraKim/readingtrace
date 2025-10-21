package com.yurakim.readingtrace.book.mapper;

import com.yurakim.readingtrace.book.dto.UserReadingStatusDto;
import com.yurakim.readingtrace.book.entity.UserReadingStatus;
import org.springframework.stereotype.Component;

@Component
public class UserReadingStatusMapper {

    public UserReadingStatus mapToEntity(UserReadingStatusDto ursDto) {
        UserReadingStatus urs = new UserReadingStatus();
        urs.setId(ursDto.getUserReadingStatusId());
        urs.setUserId(ursDto.getUserId());
        urs.setShelfId(ursDto.getShelfId());
        urs.setBookId(ursDto.getBookId());
        urs.setStatus(ursDto.getStatus());
        urs.setVisibility(ursDto.getVisibility());
        urs.setRating(ursDto.getRating());
        urs.setStartDate(ursDto.getStartDate());
        urs.setEndDate(ursDto.getEndDate());
        //TODO: check if setting metadata is needed
        //urs.setMetadata(ursDto.getUserReadingStatusMetadata());
        return urs;
    }

    public UserReadingStatus mapToEntity(UserReadingStatusDto ursDto, UserReadingStatus urs) {
        urs.setId(ursDto.getUserReadingStatusId());
        urs.setUserId(ursDto.getUserId());
        urs.setShelfId(ursDto.getShelfId());
        urs.setBookId(ursDto.getBookId());
        urs.setStatus(ursDto.getStatus());
        urs.setVisibility(ursDto.getVisibility());
        urs.setRating(ursDto.getRating());
        urs.setStartDate(ursDto.getStartDate());
        urs.setEndDate(ursDto.getEndDate());
        //TODO: check if setting metadata is needed
        //urs.setMetadata(ursDto.getUserReadingStatusMetadata());
        return urs;
    }

    public UserReadingStatusDto mapToDto(UserReadingStatus urs){
        UserReadingStatusDto ursDto = new UserReadingStatusDto();
        ursDto.setUserReadingStatusId(urs.getId());
        ursDto.setUserId(urs.getUserId());
        ursDto.setShelfId(urs.getShelfId());
        ursDto.setBookId(urs.getBookId());
        ursDto.setStatus(urs.getStatus());
        ursDto.setVisibility(urs.getVisibility());
        ursDto.setRating(urs.getRating());
        ursDto.setStartDate(urs.getStartDate());
        ursDto.setEndDate(urs.getEndDate());
        ursDto.setUserReadingStatusMetadata(urs.getMetadata());
        ursDto.setUserReadingStatusCreatedAt(urs.getCreatedAt());
        ursDto.setUserReadingStatusUpdatedAt(urs.getUpdatedAt());
        return ursDto;
    }

}
