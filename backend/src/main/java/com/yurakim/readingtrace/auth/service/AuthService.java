package com.yurakim.readingtrace.auth.service;

import com.yurakim.readingtrace.auth.dto.LoginDto;
import com.yurakim.readingtrace.auth.dto.RegisterDto;

public interface AuthService {

    String login (LoginDto loginDto);

    String register (RegisterDto registerDto);

}
