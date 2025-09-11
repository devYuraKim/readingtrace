package com.yurakim.readingtrace.user.service;

import com.yurakim.readingtrace.auth.dto.LoginResponseDto;
import com.yurakim.readingtrace.user.dto.UserDto;

import java.util.List;

public interface UserService {

    LoginResponseDto getUser(Long id);

    List<UserDto> getAllUsers();

    UserDto updateUserRoles(Long id, List<String> roleList);

}
