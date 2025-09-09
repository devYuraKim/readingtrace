package com.yurakim.readingtrace.user.service.serviceImpl;

import com.yurakim.readingtrace.auth.dto.LoginResponseDto;
import com.yurakim.readingtrace.user.entity.User;
import com.yurakim.readingtrace.user.repository.UserRepository;
import com.yurakim.readingtrace.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public LoginResponseDto getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        LoginResponseDto loginResponseDto = new LoginResponseDto(user.getEmail());
        return loginResponseDto;
    }
}
