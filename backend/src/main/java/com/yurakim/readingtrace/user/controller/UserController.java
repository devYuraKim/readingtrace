package com.yurakim.readingtrace.user.controller;

import com.yurakim.readingtrace.auth.dto.LoginResponseDto;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import com.yurakim.readingtrace.shared.constant.UploadType;
import com.yurakim.readingtrace.shared.service.S3Service;
import com.yurakim.readingtrace.user.repository.UserProfileRepository;
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
}