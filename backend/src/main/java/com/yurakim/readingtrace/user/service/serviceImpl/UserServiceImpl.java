package com.yurakim.readingtrace.user.service.serviceImpl;

import com.yurakim.readingtrace.auth.dto.LoginResponseDto;
import com.yurakim.readingtrace.user.dto.AuthenticatedUserDto;
import com.yurakim.readingtrace.user.dto.UserDto;
import com.yurakim.readingtrace.user.entity.Role;
import com.yurakim.readingtrace.user.entity.User;
import com.yurakim.readingtrace.user.repository.RoleRepository;
import com.yurakim.readingtrace.user.repository.UserRepository;
import com.yurakim.readingtrace.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public LoginResponseDto getUser(Long id, String email) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!user.getEmail().equals(email)) {throw new AccessDeniedException("Not authorized to access this user");}
        return new LoginResponseDto(user.getEmail());
    }

    //TODO: Set a mapper
    @Override
    public List<UserDto> getAllUsers() {
        List<User> allUsersEntity = userRepository.findAll();
        List<UserDto> allUsersDto = allUsersEntity.stream().map(
                user -> {
                    UserDto userDto = new UserDto();
                    userDto.setEmail(user.getEmail());
                    userDto.setRoles(user.getRoles());
                    userDto.setLastLoginAt(user.getLastLoginAt());
                    userDto.setLastFailedLoginAt(user.getLastFailedLoginAt());
                    userDto.setFailedLoginAttempts(user.getFailedLoginAttempts());
                    userDto.setFailedLoginReason(user.getFailedLoginReason());
                    userDto.setIsAccountLocked(user.getIsAccountLocked());
                    userDto.setAccountLockedAt(user.getAccountLockedAt());
                    userDto.setAccountUnlockedAt(user.getAccountUnlockedAt());
                    userDto.setOAuth2Provider(user.getOAuth2Provider());
                    userDto.setOAuth2Login(user.getOAuth2Login());
                    return userDto;
                }
        ).toList();
        return allUsersDto;
    }

    @Override
    public UserDto updateUserRoles(Long id, List<String> roleList) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<Role> roles = roleList.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + roleName)))
                .collect(Collectors.toSet());
        user.setRoles(roles);
        userRepository.save(user);

        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());
        userDto.setLastLoginAt(user.getLastLoginAt());
        userDto.setLastFailedLoginAt(user.getLastFailedLoginAt());
        userDto.setFailedLoginAttempts(user.getFailedLoginAttempts());
        userDto.setFailedLoginReason(user.getFailedLoginReason());
        userDto.setIsAccountLocked(user.getIsAccountLocked());
        userDto.setAccountLockedAt(user.getAccountLockedAt());
        userDto.setAccountUnlockedAt(user.getAccountUnlockedAt());
        userDto.setOAuth2Provider(user.getOAuth2Provider());
        userDto.setOAuth2Login(user.getOAuth2Login());

        return userDto;
    }

    @Override
    public AuthenticatedUserDto getAuthenticatedUser(String email) {
        User userRecord = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        AuthenticatedUserDto user = new AuthenticatedUserDto();
        user.setUserId(userRecord.getId());
        user.setEmail(userRecord.getEmail());
        user.setRoles(userRecord.getRoles());
        return user;
    }
}
