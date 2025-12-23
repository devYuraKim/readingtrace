package com.yurakim.readingtrace.directmessage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarkReadDto {

    Long scrolledUserId;
    Long notifiedUserId;
    LocalDateTime scrolledAt;

}
