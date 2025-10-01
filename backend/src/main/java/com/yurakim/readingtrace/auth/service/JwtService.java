package com.yurakim.readingtrace.auth.service;

import com.yurakim.readingtrace.auth.entity.entity.RefreshToken;
import com.yurakim.readingtrace.user.entity.User;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;

public interface JwtService {

    String generateAccessToken(Authentication authentication);

    void validateAccessToken(String token);

    String generateRefreshToken(User user, LocalDateTime rotateExpiry);

    String getSubject(String token);

    RefreshToken validateRefreshToken(String token);

    String refreshAccessToken(String refreshToken);

    String rotateRefreshToken(String oldRefreshToken);

    @Deprecated
    String generateJwt(Authentication authentication);

    @Deprecated
    Boolean isValidRefreshToken(String refreshToken);
}
