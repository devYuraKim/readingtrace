package com.yurakim.readingtrace.directmessage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebRTCMessage {

    private Long senderId;
    private Long targetUserId;
    private String type; // "offer", "answer", "ice"
    private String sdp; // optional: only for offer/answer
    private String candidate; // optional: only for ICE

}
