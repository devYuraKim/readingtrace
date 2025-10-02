package com.yurakim.readingtrace.chat.controller;

import com.yurakim.readingtrace.chat.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpleMessagingTemplate;

    @MessageMapping("/support.sendMessage")
    @SendTo("/topic/support")
    public void supportChat(ChatMessage chatMessage){
        log.info("Support chat message: {}", chatMessage);
        //send to recipient's private queue
        simpleMessagingTemplate.convertAndSendToUser(
                chatMessage.getRecipient(),
                "/queue/support",
                chatMessage
        );
    }

}
