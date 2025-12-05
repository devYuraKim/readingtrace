package com.yurakim.readingtrace.user.repository;

import com.yurakim.readingtrace.user.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    boolean existsByNickname(String nickname);
    UserProfile findByUserId(Long userId);
    List<UserProfile> findAllByUserIdNot(Long userId);
}
