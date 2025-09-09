package com.yurakim.readingtrace.user.service;

import com.yurakim.readingtrace.auth.dto.LoginResponseDto;

public interface UserService {

    LoginResponseDto getUser(Long id);

}
