package com.yurakim.readingtrace.chat.service.serviceImpl;

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
    private final Set<Long> onlineUserIds = ConcurrentHashMap.newKeySet();

    public void addOnlineUserId(Long userId) {
        onlineUserIds.add(userId);
        messagingTemplate.convertAndSend("/topic/presence", onlineUserIds);
    }

    public void removeOnlineUserId(Long userId) {
        onlineUserIds.remove(userId);
        messagingTemplate.convertAndSend("/topic/presence", onlineUserIds);
    }

}