package com.yurakim.readingtrace.auth.service;

import com.yurakim.readingtrace.auth.dto.LoginDto;
import com.yurakim.readingtrace.auth.dto.RegisterDto;

public interface IAuthService {

    String login (LoginDto loginDto);

    String register (RegisterDto registerDto);

}
