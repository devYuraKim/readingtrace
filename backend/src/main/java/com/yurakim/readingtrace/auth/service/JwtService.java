package com.yurakim.readingtrace.auth.service;

import com.yurakim.readingtrace.auth.entity.entity.RefreshToken;
import com.yurakim.readingtrace.user.entity.User;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;

public interface JwtService {

    @Deprecated
    String generateJwt(Authentication authentication);

    String generateAccessToken(Authentication authentication);

    String generateRefreshToken(User user, LocalDateTime rotateExpiry);

    Boolean isValidRefreshToken(String refreshToken);

    String getSubject(String token);

    RefreshToken validateRefreshToken(String token);

    String refreshAccessToken(String refreshToken);

    String rotateRefreshToken(String oldRefreshToken);
}
