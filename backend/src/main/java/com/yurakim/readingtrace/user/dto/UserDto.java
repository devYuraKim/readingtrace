package com.yurakim.readingtrace.user.dto;

import com.yurakim.readingtrace.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {

    private String email;
    private Set<Role> roles;
    private LocalDateTime lastLoginAt;
    private LocalDateTime lastFailedLoginAt;
    private Integer failedLoginAttempts = 0;
    private String failedLoginReason;
    private Boolean isAccountLocked = false;
    private LocalDateTime accountLockedAt;
    private LocalDateTime accountUnlockedAt;
    private Boolean oAuth2Login;
    private String oAuth2Provider;

}
