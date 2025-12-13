package com.yurakim.readingtrace.chat.service;

public interface PresenceService {

    void userOnline(Long userId);

    void userOffline(Long userId);

}
