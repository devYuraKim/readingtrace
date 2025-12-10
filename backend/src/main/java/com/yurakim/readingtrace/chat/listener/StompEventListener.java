package com.yurakim.readingtrace.chat.listener;

import com.yurakim.readingtrace.chat.service.PresenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompEventListener {

    private final ConcurrentHashMap<String, Long> sessionUserMap = new ConcurrentHashMap<>();
    private final PresenceService presenceService;
    private final SimpMessagingTemplate messagingTemplate;

    private final ConcurrentHashMap<String, String> sessionMap = new ConcurrentHashMap<>();
    public Set<String> getSessions() {
        return sessionMap.keySet();
    }

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        System.out.println(String.format("PLEASE WORK %s", accessor));
        Authentication auth = (Authentication) accessor.getUser();

        System.out.println(String.format("WHAT IS AUTH: %s", auth));

        if(auth != null){
            String email = auth.getName();
            System.out.println(String.format("WHAT USER ID: %s", email));
            String sessionId = accessor.getSessionId();

//            sessionUserMap.put(sessionId, email);
//            presenceService.userOnline(userId);

//            broadcastPresence();

        }
    }

    private void broadcastPresence() {
        Set<Long> onlineUsers = presenceService.getOnlineUsers();
        System.out.println(">>> Broadcasting presence: " + onlineUsers);
        messagingTemplate.convertAndSend("/topic/presence", onlineUsers);
    }

//    @EventListener
//    public void listener(SessionConnectEvent sessionConnectEvent) {
//        log.info("sessionConnectEvent. {}", sessionConnectEvent);
//
//    }

    @EventListener
    public void listener(SessionConnectedEvent sessionConnectedEvent) {
        log.info("sessionConnectedEvent. {}", sessionConnectedEvent);
        String sessionId = sessionConnectedEvent.getMessage().getHeaders().get("simpSessionId").toString();
        sessionMap.put(sessionId, sessionId);
    }

    @EventListener
    public void listener(SessionSubscribeEvent sessionSubscribeEvent) {
        log.info("sessionSubscribeEvent. {}", sessionSubscribeEvent);
    }

    @EventListener
    public void listener(SessionUnsubscribeEvent sessionUnsubscribeEvent) {
        log.info("sessionUnsubscribeEvent. {}", sessionUnsubscribeEvent);
    }

    @EventListener
    public void listener(SessionDisconnectEvent sessionDisconnectEvent) {
        log.info("sessionDisconnectEvent. {}", sessionDisconnectEvent);
        String sessionId = sessionDisconnectEvent.getMessage().getHeaders().get("simpSessionId").toString();
        sessionMap.remove(sessionId);
    }

}
