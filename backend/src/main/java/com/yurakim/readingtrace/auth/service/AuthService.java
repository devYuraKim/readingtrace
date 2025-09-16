package com.yurakim.readingtrace.auth.service;

import com.yurakim.readingtrace.auth.dto.AccessRefreshDto;
import com.yurakim.readingtrace.auth.dto.LoginRequestDto;
import com.yurakim.readingtrace.auth.dto.PasswordResetDto;
import com.yurakim.readingtrace.auth.dto.SignupDto;

public interface AuthService {

    AccessRefreshDto login (LoginRequestDto loginDto);

    String signup (SignupDto signupDto);

    String generatePasswordResetToken(String email);

    String resetPassword(PasswordResetDto passwordResetDto);
}
