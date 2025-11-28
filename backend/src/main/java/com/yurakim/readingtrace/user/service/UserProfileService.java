package com.yurakim.readingtrace.user.service;

import com.yurakim.readingtrace.user.entity.UserProfile;

public interface UserProfileService {

    UserProfile getUserProfileByUserId(Long userId);
}
