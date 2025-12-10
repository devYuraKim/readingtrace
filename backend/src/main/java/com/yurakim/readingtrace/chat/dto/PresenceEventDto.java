package com.yurakim.readingtrace.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PresenceEventDto {

    private Long userId;
    private String status;

}
