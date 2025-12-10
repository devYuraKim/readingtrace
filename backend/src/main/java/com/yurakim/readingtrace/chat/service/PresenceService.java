package com.yurakim.readingtrace.chat.service;

import java.util.Set;

public interface PresenceService {

    void userOnline(Long userId);

    void userOffline(Long userId);

    Set<Long> getOnlineUsers();
}
