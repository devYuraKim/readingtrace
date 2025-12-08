package com.yurakim.readingtrace.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserProfileResponseDto {

    private Long userProfileId;
    private Long userId;
    private String nickname;
    private String profileImageUrl;
    private Long readingGoalCount;
    private String readingGoalUnit;
    private String readingGoalTimeframe;
    private String favoredGenres;
    private Boolean isOnboardingCompleted;
    private Boolean isFriend;
    private Set<Long> bookIds;
}
