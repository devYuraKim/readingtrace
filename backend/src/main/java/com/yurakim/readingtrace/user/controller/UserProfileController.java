package com.yurakim.readingtrace.user.controller;

import com.yurakim.readingtrace.shared.constant.ApiPath;
import com.yurakim.readingtrace.user.dto.UserProfileResponseDto;
import com.yurakim.readingtrace.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(ApiPath.USERPROFILE) // /api/v1/users/{userId}/profiles
@RestController
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public List<UserProfileResponseDto> getAllUserProfiles(@PathVariable Long userId) {
        return userProfileService.getAllUserProfilesExceptUserId(userId);
    }
}
