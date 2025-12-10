package com.yurakim.readingtrace.chat.service.serviceImpl;

import com.yurakim.readingtrace.chat.dto.PresenceEventDto;
import com.yurakim.readingtrace.chat.service.PresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class PresenceServiceImpl implements PresenceService {

    private final SimpMessagingTemplate messagingTemplate;
    private final Set<Long> onlineUsers = ConcurrentHashMap.newKeySet();


    public void userOnline(Long userId) {
        messagingTemplate.convertAndSend("/topic/presence",
                new PresenceEventDto(userId, "ONLINE"));
    }

    public void userOffline(Long userId) {
        messagingTemplate.convertAndSend("/topic/presence",
                new PresenceEventDto(userId, "OFFLINE"));
    }

    public Set<Long> getOnlineUsers() {
        return onlineUsers;
    }

}