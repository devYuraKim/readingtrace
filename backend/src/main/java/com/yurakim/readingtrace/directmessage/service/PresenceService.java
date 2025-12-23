package com.yurakim.readingtrace.directmessage.service;

public interface PresenceService {

    void addOnlineUserId(Long userId);

    void removeOnlineUserId(Long userId);

}
