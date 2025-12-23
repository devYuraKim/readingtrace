package com.yurakim.readingtrace.directmessage.listener;

import com.yurakim.readingtrace.directmessage.service.PresenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.security.Principal;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompEventListener {

    private final ConcurrentHashMap<String, Long> sessionUserMap = new ConcurrentHashMap<>();
    private final PresenceService presenceService;

    private final ConcurrentHashMap<String, String> sessionMap = new ConcurrentHashMap<>();
    public Set<String> getSessions() {
        return sessionMap.keySet();
    }

    //SessionConnected에서는 onlineUserIds를 message로 보내도 subscribe 되기 이전이라 유실되므로 해당 message 전달은 subscribe 이후에 진행
    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event){
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        Principal principal = accessor.getUser();
        Long userId = Long.valueOf(principal.getName());
        String sessionId = accessor.getSessionId();
        sessionUserMap.put(sessionId, userId);
        System.out.println("CONNECTED SESSION USER MAP: " + sessionUserMap);

        //StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        //Authentication auth = (Authentication) accessor.getUser();
        //Object principal = auth.getPrincipal();
        //if(principal instanceof UserDetailsImpl userDetails) {
        //    String email = auth.getName();
        //    Long userId = userDetails.getId();
        //    String sessionId = accessor.getSessionId();
        //    sessionUserMap.put(sessionId, userId);
        //    presenceService.userOnline(userId);
        //}
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        Long userId = sessionUserMap.get(sessionId);
        if ("/topic/presence".equals(accessor.getDestination())) {
            System.out.println("DESTINATION: " + accessor.getDestination());
            if (userId != null) presenceService.addOnlineUserId(userId);
        }
        //SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        //Principal principal = accessor.getUser();
        //Long userId = Long.valueOf(principal.getName());
        //if ("/topic/presence".equals(accessor.getDestination())) presenceService.addOnlineUserId(userId);
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        Long userId = sessionUserMap.remove(sessionId);
        if (userId != null) presenceService.removeOnlineUserId(userId);
    }

//    @EventListener
//    public void listener(SessionConnectEvent sessionConnectEvent) {
//        log.info("sessionConnectEvent. {}", sessionConnectEvent);
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
