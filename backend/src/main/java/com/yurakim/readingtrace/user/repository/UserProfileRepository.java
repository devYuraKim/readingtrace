package com.yurakim.readingtrace.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yurakim.readingtrace.user.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    boolean existsByNickname(String nickname);
    UserProfile findByUserId(Long userId);

}
