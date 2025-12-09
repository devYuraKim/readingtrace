package com.yurakim.readingtrace.user.service;

import com.yurakim.readingtrace.user.dto.ChatProfileDto;
import com.yurakim.readingtrace.user.dto.UserProfileResponseDto;
import com.yurakim.readingtrace.user.entity.UserProfile;

import java.util.List;
import java.util.Set;

public interface UserProfileService {

    UserProfile getUserProfileByUserId(Long userId);

    List<UserProfileResponseDto> getAllUserProfilesExceptUserId(Long userId);

    List<ChatProfileDto> getAllChatProfilesByIds(Set<Long> userIds);
}
