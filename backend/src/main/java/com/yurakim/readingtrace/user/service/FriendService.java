package com.yurakim.readingtrace.user.service;

public interface FriendService {
    void toggleFollow(Long followingUserId, Long followedUserId);
}
