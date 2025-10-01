package com.yurakim.readingtrace.user.service;

import com.yurakim.readingtrace.auth.dto.LoginResponseDto;
import com.yurakim.readingtrace.user.dto.AuthenticatedUserDto;
import com.yurakim.readingtrace.user.dto.UserDto;

import java.util.List;

public interface UserService {

    LoginResponseDto getUser(Long userId, String email);

    List<UserDto> getAllUsers();

    UserDto updateUserRoles(Long userId, List<String> roleList);

    AuthenticatedUserDto getAuthenticatedUser(String email);

}
