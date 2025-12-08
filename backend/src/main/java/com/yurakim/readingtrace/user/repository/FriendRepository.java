package com.yurakim.readingtrace.user.repository;

import com.yurakim.readingtrace.user.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    Optional<Friend> findByFollowingUserIdAndFollowedUserId(Long followingUserId, Long followedUserId);
    Set<Friend> findByFollowingUserIdAndFollowedUserIdIn(Long followingUserId, Set<Long> followedUserId);
}
