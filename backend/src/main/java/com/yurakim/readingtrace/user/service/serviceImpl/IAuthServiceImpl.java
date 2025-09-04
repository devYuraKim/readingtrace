package com.yurakim.readingtrace.user.service.serviceImpl;

import com.yurakim.readingtrace.user.dto.LoginDto;
import com.yurakim.readingtrace.user.dto.RegisterDto;
import com.yurakim.readingtrace.user.entity.Role;
import com.yurakim.readingtrace.user.entity.User;
import com.yurakim.readingtrace.user.repository.RoleRepository;
import com.yurakim.readingtrace.user.repository.UserRepository;
import com.yurakim.readingtrace.user.service.IAuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class IAuthServiceImpl implements IAuthService {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(LoginDto loginDto) {
        userDetailsService.loadUserByUsername(loginDto.getEmail());
        return "Logged in";
    }

    @Override
    public String register(RegisterDto registerDto) {
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("USER").get());
        user.setRoles(roles);

        userRepository.save(user);

        return "User registered";
    }
}
