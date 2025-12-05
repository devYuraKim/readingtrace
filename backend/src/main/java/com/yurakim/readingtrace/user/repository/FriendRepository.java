package com.yurakim.readingtrace.user.repository;

import com.yurakim.readingtrace.user.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    Optional<Friend> findByFollowingUserIdAndFollowedUserId(Long followingUserId, Long followedUserId);
}
