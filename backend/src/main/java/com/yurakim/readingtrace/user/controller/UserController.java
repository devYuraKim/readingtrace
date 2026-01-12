package com.yurakim.readingtrace.user.controller;

import com.yurakim.readingtrace.auth.dto.LoginResponseDto;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import com.yurakim.readingtrace.shared.constant.UploadType;
import com.yurakim.readingtrace.shared.service.S3Service;
import com.yurakim.readingtrace.user.dto.UserProfileRequestDto;
import com.yurakim.readingtrace.user.dto.UserProfileResponseDto;
import com.yurakim.readingtrace.user.entity.UserProfile;
import com.yurakim.readingtrace.user.repository.UserProfileRepository;
import com.yurakim.readingtrace.user.service.UserProfileService;
import com.yurakim.readingtrace.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping(ApiPath.USER) // api/v1/users
public class UserController {

    private final UserService userService;
    private final UserProfileRepository userProfileRepository;
    private final UserProfileService userProfileService;
    @Lazy
    private final S3Service s3Service;

    @GetMapping("/{id}")
    public ResponseEntity<LoginResponseDto> getUser(@PathVariable("id") Long id, @AuthenticationPrincipal String email){
        LoginResponseDto loginResponseDto = userService.getUser(id, email);
        return ResponseEntity.ok(loginResponseDto);
    }

    @GetMapping("/{userId}/onboarding")
    public ResponseEntity<Boolean> checkUserNickname(@PathVariable Long userId, @RequestParam int step, @RequestParam String nickname){
        String cleanedNickname = nickname
                .trim()
                .toLowerCase()
                .replaceAll("\\s+", "")
                .replaceAll("[^a-z0-9_]", "");
        boolean nicknameAvailability = !userProfileRepository.existsByNickname(cleanedNickname);
        return ResponseEntity.ok(nicknameAvailability);
    }

    @PostMapping("/{userId}/temp-profile-image")
    public ResponseEntity<Map<String, String>> uploadTempProfileImage(@PathVariable Long userId, @RequestBody MultipartFile profileImageFile) {
        String fileUrl = s3Service.uploadFile(userId, profileImageFile, UploadType.TEMP);
        return ResponseEntity.ok(Map.of("url", fileUrl));
    }

    @PostMapping("/{userId}/onboarding")
    public ResponseEntity<Void> createUserProfile(@PathVariable Long userId, @RequestBody UserProfileRequestDto userProfileRequestDto) {

        UserProfile userProfile = userProfileService.getUserProfileByUserId(userId);

        userProfile.setNickname(userProfileRequestDto.getNickname());
        //TODO: profile image temp에서 확정 파일로 이동하는 로직 필요
        if(userProfileRequestDto.getProfileImageUrl()==null) {
            userProfile.setProfileImageUrl("https://readingtrace-bucket.s3.ap-northeast-2.amazonaws.com/user/profile/final/default.png");
        }
        else {
            userProfile.setProfileImageUrl(s3Service.moveTempToPermanent(userId, userProfileRequestDto.getProfileImageUrl()));
        }
        userProfile.setReadingGoalCount(userProfileRequestDto.getReadingGoalCount());
        userProfile.setReadingGoalUnit(userProfileRequestDto.getReadingGoalUnit());
        userProfile.setReadingGoalTimeframe(userProfileRequestDto.getReadingGoalTimeframe());
        userProfile.setFavoredGenres(userProfileRequestDto.getFavoredGenres());
        userProfile.setIsOnboardingCompleted(true);
        userProfileRepository.save(userProfile);

        return null;
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfileResponseDto> getUserProfile(@PathVariable Long userId) {
        UserProfile entity = userProfileService.getUserProfileByUserId(userId);
        UserProfileResponseDto dto = new UserProfileResponseDto();
        if(entity.getIsOnboardingCompleted() == true) {
            //TODO: handle case where isOnboardingCompleted == false
            dto.setUserProfileId(entity.getId());
            dto.setUserId(userId);
            dto.setNickname(entity.getNickname());
            dto.setProfileImageUrl(entity.getProfileImageUrl());
            dto.setReadingGoalCount(entity.getReadingGoalCount());
            dto.setReadingGoalUnit(entity.getReadingGoalUnit());
            dto.setReadingGoalTimeframe(entity.getReadingGoalTimeframe());
            dto.setFavoredGenres(entity.getFavoredGenres());
            dto.setIsOnboardingCompleted(entity.getIsOnboardingCompleted());
        }
        return ResponseEntity.ok(dto);
    }
}