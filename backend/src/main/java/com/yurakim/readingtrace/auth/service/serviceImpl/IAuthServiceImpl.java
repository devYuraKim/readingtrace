package com.yurakim.readingtrace.auth.service.serviceImpl;

import com.yurakim.readingtrace.auth.dto.LoginDto;
import com.yurakim.readingtrace.auth.dto.RegisterDto;
import com.yurakim.readingtrace.auth.service.IAuthService;
import com.yurakim.readingtrace.user.entity.Role;
import com.yurakim.readingtrace.user.entity.User;
import com.yurakim.readingtrace.user.repository.RoleRepository;
import com.yurakim.readingtrace.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

//TODO: add method for unlocking account

@Slf4j
@AllArgsConstructor
@Service
public class IAuthServiceImpl implements IAuthService {

    private static final int MAX_FAILED_ATTEMPTS = 5;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public String login(LoginDto loginDto) {
        //TODO: check if controller method has proper validation
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        //CHECK IF ACCOUNT IS LOCKED
        if (user.getIsAccountLocked()) {
            throw new LockedException("Account is locked");
        }
        //IF ACCOUNT IS NOT LOCKED, CONTINUE WITH LOGIN PROCESS
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, password);
        try {
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //RECORD LOGIN SUCCESS
            recordLoginSuccess(user);
            return "Logged in";
        } catch (AuthenticationException e) {
            recordLoginFailure(user, e);
            return String.format("Login failed (%d/%d)", user.getFailedLoginAttempts(), MAX_FAILED_ATTEMPTS);
        }
    }

    //TODO: implement a hybrid strategy
    //Keep synchronous: failedLoginAttempts, isAccountLocked, accountLockedAtsecurity (enforcing state changes. Must persist before response.)
    //Make async: lastLoginAt, lastFailedLoginAt, failedLoginReason (for logging/analytics only. observability data, not security-critical.)
    //Use Spring ApplicationEvents (or Spring Security’s AuthenticationSuccessEvent / AuthenticationFailureBadCredentialsEvent). Listener can be @Async, and write to DB / external log / metrics.

    private void recordLoginSuccess(User user){
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
    }

    private void recordLoginFailure(User user, Exception exception){
        user.setLastFailedLoginAt(LocalDateTime.now());
        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
        user.setFailedLoginReason(exception.getMessage());
        if (user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS && !user.getIsAccountLocked()) {
            user.setIsAccountLocked(true);
            user.setAccountLockedAt(LocalDateTime.now());
        }
        userRepository.save(user);
        log.warn("Login failed for user {}: {}", user.getEmail(), exception.getMessage());
    }

    @Override
    @Transactional
    public String register(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
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