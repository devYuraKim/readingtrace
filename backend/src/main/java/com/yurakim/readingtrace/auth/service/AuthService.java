package com.yurakim.readingtrace.auth.service;

import com.yurakim.readingtrace.auth.dto.LoginRequestDto;
import com.yurakim.readingtrace.auth.dto.RegisterDto;

public interface AuthService {

    String login (LoginRequestDto loginDto);

    String register (RegisterDto registerDto);

}
