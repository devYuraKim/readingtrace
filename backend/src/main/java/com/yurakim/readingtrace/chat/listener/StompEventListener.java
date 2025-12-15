package com.yurakim.readingtrace.chat.listener;

import com.yurakim.readingtrace.auth.security.UserDetailsImpl;
import com.yurakim.readingtrace.chat.service.PresenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final SimpMessagingTemplate messagingTemplate;

    private final ConcurrentHashMap<String, String> sessionMap = new ConcurrentHashMap<>();
    public Set<String> getSessions() {
        return sessionMap.keySet();
    }

    //SessionConnected에서는 message 보내도 subscribe 되기 이전이라 유실되므로 메시지 전달은 subscribe 이후에 진행
    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event){
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
//        Authentication auth = (Authentication) accessor.getUser();
//        Object principal = auth.getPrincipal();
//        if(principal instanceof UserDetailsImpl userDetails) {
//            String email = auth.getName();
//            Long userId = userDetails.getId();
//            String sessionId = accessor.getSessionId();
//            sessionUserMap.put(sessionId, userId);
//            presenceService.userOnline(userId);
//        }

        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        Principal principal = accessor.getUser();
        System.out.println("CONNECTED PRINCIPAL: " + principal.toString());
        if (principal == null) System.out.println("CONNECTED: PRINCIPAL IS NULL!!!!!");
        if (principal instanceof UsernamePasswordAuthenticationToken auth) {
            System.out.println("CONNECTED CLASS OF auth VARIABLE: " + auth.getClass());
            Object p = auth.getPrincipal();
            System.out.println("CONNECTED CLASS of p VARIABLE: " + p.getClass());
            if (p instanceof UserDetailsImpl userDetails) {
                Long userId = userDetails.getId();
                String sessionId = accessor.getSessionId();
                System.out.println("CONNECTED userId = " + userId);
                System.out.println("CONNECTED sessionId = " + sessionId);
                sessionUserMap.put(sessionId, userId);
                System.out.println("CONNECTED SESSION USER MAP: " + sessionUserMap);
            }
        }
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
//        Authentication auth = (Authentication) accessor.getUser();
//        if (auth == null) System.out.println("SUBSCRIBE: AUTH IS NULL");
//        String sessionId = accessor.getSessionId();
//        Long userId = sessionUserMap.get(sessionId);
//        if ("/topic/presence".equals(accessor.getDestination())) {
//            presenceService.userOnline(userId);
//        }

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        Long userId = sessionUserMap.get(sessionId);
        if ("/topic/presence".equals(accessor.getDestination())) {
            System.out.println("DESTINATION: " + accessor.getDestination());
            if (userId != null) presenceService.addOnlineUserId(userId);
        }

//        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
//        Principal principal = accessor.getUser();
//        if (principal == null) System.out.println("SUBSCRIBE: PRINCIPAL IS NULL!!!!!");
//        if (principal instanceof UsernamePasswordAuthenticationToken auth) {
//            Object p = auth.getPrincipal();
//            if (p instanceof UserDetailsImpl userDetails) {
//                Long userId = userDetails.getId();
//                if ("/topic/presence".equals(accessor.getDestination())) presenceService.addOnlineUserId(userId);
//            }
//        }
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
