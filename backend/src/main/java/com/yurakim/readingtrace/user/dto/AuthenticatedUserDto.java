package com.yurakim.readingtrace.user.dto;

import com.yurakim.readingtrace.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthenticatedUserDto {

    private Long userId;
    private String email;
    private Set<Role> roles;
    private Boolean nickname;
    private String profileImageUrl;
    private Long readingGoal;
    private String favoredGenres;
    private Boolean isOnboardingCompleted;

}
