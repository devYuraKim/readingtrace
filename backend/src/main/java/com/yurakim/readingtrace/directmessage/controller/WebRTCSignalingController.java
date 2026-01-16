package com.yurakim.readingtrace.directmessage.controller;

import com.yurakim.readingtrace.directmessage.dto.WebRTCMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;


@Controller
@RequiredArgsConstructor
public class WebRTCSignalingController {

    private final SimpMessagingTemplate simpleMessagingTemplate;

    @MessageMapping("/webrtc/offer")
    public void handleOffer(@Payload WebRTCMessage msg, Principal principal) {
        System.out.println("WebRTC offer");
        System.out.println("principal getname: " + principal.getName());
        Long senderId = Long.valueOf(principal.getName());
        msg.setSenderId(senderId);

        // Forward the offer to the target user
        simpleMessagingTemplate.convertAndSendToUser(
                msg.getTargetUserId().toString(),
                "/queue/webrtc",
                msg
        );
    }

    @MessageMapping("/webrtc/answer")
    public void handleAnswer(@Payload WebRTCMessage msg, Principal principal) {
        Long senderId = Long.valueOf(principal.getName());
        msg.setSenderId(senderId);

        // Forward the answer to the target user
        simpleMessagingTemplate.convertAndSendToUser(
                msg.getTargetUserId().toString(),
                "/queue/webrtc",
                msg
        );
    }

    @MessageMapping("/webrtc/ice")
    public void handleIceCandidate(@Payload WebRTCMessage msg, Principal principal) {
        Long senderId = Long.valueOf(principal.getName());
        msg.setSenderId(senderId);

        // Forward ICE candidate to the target user
        simpleMessagingTemplate.convertAndSendToUser(
                msg.getTargetUserId().toString(),
                "/queue/webrtc",
                msg
        );
    }


}
