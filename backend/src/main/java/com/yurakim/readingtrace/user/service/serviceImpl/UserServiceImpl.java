package com.yurakim.readingtrace.user.service.serviceImpl;

import com.yurakim.readingtrace.auth.dto.LoginResponseDto;
import com.yurakim.readingtrace.user.dto.AuthenticatedUserDto;
import com.yurakim.readingtrace.user.dto.UserDto;
import com.yurakim.readingtrace.user.entity.Role;
import com.yurakim.readingtrace.user.entity.User;
import com.yurakim.readingtrace.user.entity.UserProfile;
import com.yurakim.readingtrace.user.repository.RoleRepository;
import com.yurakim.readingtrace.user.repository.UserProfileRepository;
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
    private final UserProfileRepository userProfileRepository;

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
        //TODO 방법1: profileService 만들어서 profile 정보 AuthenticatedUserDto에 추가
        //TODO 방법2: getAuthenticatedUser()의 repository method를 join table record 반환하도록 query 작성
        User userRecord = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found for email: " + email));
        UserProfile userProfile = userProfileRepository.findById(userRecord.getId()).orElseThrow(() -> new UsernameNotFoundException("UserProfile not found for userId: " + userRecord.getId()));

        AuthenticatedUserDto authUserDto = new AuthenticatedUserDto();
        authUserDto.setUserId(userRecord.getId());
        authUserDto.setEmail(userRecord.getEmail());
        authUserDto.setRoles(userRecord.getRoles());
        authUserDto.setNickname(userProfile.getNickname());
        authUserDto.setFavoredGenres(userProfile.getFavoredGenres());
        authUserDto.setProfileImageUrl(userProfile.getProfileImageUrl());
        authUserDto.setIsOnboardingCompleted(userProfile.getIsOnboardingCompleted());
        authUserDto.setReadingGoalCount(userProfile.getReadingGoalCount());
        authUserDto.setReadingGoalUnit(userProfile.getReadingGoalUnit());
        authUserDto.setReadingGoalTimeframe(userProfile.getReadingGoalTimeframe());
        return authUserDto;
    }

}
