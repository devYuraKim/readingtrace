package com.yurakim.readingtrace.user.service.serviceImpl;

import com.yurakim.readingtrace.user.dto.UserProfileResponseDto;
import com.yurakim.readingtrace.user.entity.UserProfile;
import com.yurakim.readingtrace.user.repository.UserProfileRepository;
import com.yurakim.readingtrace.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserProfileServiceImpl implements UserProfileService {

   private final UserProfileRepository userProfileRepository;

   @Override
   public UserProfile getUserProfileByUserId(Long userId){
       return userProfileRepository.findByUserId(userId);
   }

    @Override
    public List<UserProfileResponseDto> getAllUserProfilesExceptUserId(Long userId) {
       return userProfileRepository.findAllByUserIdNot(userId).stream().map(userProfile -> {
            UserProfileResponseDto dto = new UserProfileResponseDto();
            dto.setUserProfileId(userProfile.getId());
            dto.setUserId(userProfile.getUser().getId());
            dto.setNickname(userProfile.getNickname());
            dto.setProfileImageUrl(userProfile.getProfileImageUrl());
            dto.setReadingGoalCount(userProfile.getReadingGoalCount());
            dto.setReadingGoalUnit(userProfile.getReadingGoalUnit());
            dto.setReadingGoalTimeframe(userProfile.getReadingGoalTimeframe());
            dto.setFavoredGenres(userProfile.getFavoredGenres());
            dto.setIsOnboardingCompleted(userProfile.getIsOnboardingCompleted());
            return dto;
       }).toList();
    }


}
