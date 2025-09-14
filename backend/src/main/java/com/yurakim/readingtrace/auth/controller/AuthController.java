package com.yurakim.readingtrace.auth.controller;

import com.yurakim.readingtrace.auth.constant.JWT;
import com.yurakim.readingtrace.auth.dto.LoginRequestDto;
import com.yurakim.readingtrace.auth.dto.PasswordResetDto;
import com.yurakim.readingtrace.auth.dto.SignupDto;
import com.yurakim.readingtrace.auth.service.AuthService;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(ApiPath.AUTH)
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignupDto signupDto) {
        String message = authService.signup(signupDto);
        return ResponseEntity.ok(message);
    }

    //TODO: fix return type
    //TODO: add validation for loginDto
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequestDto loginDto) {
        String jwt = authService.login(loginDto);
        return ResponseEntity.ok().header(JWT.JWT_HEADER, JWT.JWT_PREFIX + jwt).build();
    }

    @GetMapping("/csrf")
    public ResponseEntity<String> getCsrfToken() {
        return ResponseEntity.ok("CSRF token");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody String email){
        String message = authService.generatePasswordResetToken(email);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetDto passwordResetDto){
        String message = authService.resetPassword(passwordResetDto);
        return ResponseEntity.ok(message);
    }

}
