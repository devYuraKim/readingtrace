package com.yurakim.readingtrace.chat.controller;

import com.yurakim.readingtrace.chat.dto.ChatMessageDto;
import com.yurakim.readingtrace.chat.listener.StompEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StompController {

    private final ConcurrentHashMap<String, ScheduledFuture<?>> scheduledFutures = new ConcurrentHashMap<>();

    private final TaskScheduler taskScheduler;
    private final SimpMessagingTemplate simpleMessagingTemplate;
    private final StompEventListener stompEventListener;

    @MessageMapping("/support.sendMessage")
    @SendTo("/topic/support")
    public ChatMessageDto supportChat(Message<ChatMessageDto> message){
        return message.getPayload();
    }

    @MessageMapping("/sessions")
    @SendToUser("/queue/sessions")
    public Set<String> sessions(MessageHeaders messageHeaders){
        //FIX: Stomp connection on app mount
        log.info(">>>>> sessions: {}", stompEventListener.getSessions());
        log.info(">>>>> source session: {}", messageHeaders.get("simpSessionId"));
        return stompEventListener.getSessions();
    }

    @MessageMapping("/start")
    public void start(Message<ChatMessageDto> message, MessageHeaders messageHeaders){
        String username = messageHeaders.get("simpUser").toString();
        ScheduledFuture<?> updateBySecond = taskScheduler.scheduleAtFixedRate(() -> {
            //1초마다 실행
            simpleMessagingTemplate.convertAndSendToUser(username, "/queue/notifications", "Hello");
        }, Duration.ofSeconds(1));
        scheduledFutures.put(username, updateBySecond);
    }

    @MessageMapping("/stop")
    public void stop(Message<ChatMessageDto> message, MessageHeaders messageHeaders){
        String username = messageHeaders.get("simpUser").toString();
        scheduledFutures.get(username).cancel(true);
        scheduledFutures.remove(username);
    }

}
