package com.yurakim.readingtrace.user.service;

import com.yurakim.readingtrace.user.dto.UserProfileResponseDto;
import com.yurakim.readingtrace.user.entity.UserProfile;

import java.util.List;

public interface UserProfileService {

    UserProfile getUserProfileByUserId(Long userId);

    List<UserProfileResponseDto> getAllUserProfilesExceptUserId(Long userId);
}
