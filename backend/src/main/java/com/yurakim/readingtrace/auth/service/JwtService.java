package com.yurakim.readingtrace.auth.service;

import com.yurakim.readingtrace.user.entity.User;
import org.springframework.security.core.Authentication;

public interface JwtService {

    String generateJwt(Authentication authentication);

    String generateAccessToken(Authentication authentication);

    String generateRefreshToken(User user);
}
