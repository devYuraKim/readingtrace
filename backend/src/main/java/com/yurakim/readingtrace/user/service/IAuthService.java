package com.yurakim.readingtrace.user.service;

import com.yurakim.readingtrace.user.dto.LoginDto;
import com.yurakim.readingtrace.user.dto.RegisterDto;

public interface IAuthService {

    String login (LoginDto loginDto);

    String register (RegisterDto registerDto);
}
