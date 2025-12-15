package com.yurakim.readingtrace.chat.service;

public interface PresenceService {

    void addOnlineUserId(Long userId);

    void removeOnlineUserId(Long userId);

}
