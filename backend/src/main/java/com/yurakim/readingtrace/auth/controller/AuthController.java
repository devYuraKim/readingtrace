package com.yurakim.readingtrace.auth.controller;

import com.yurakim.readingtrace.shared.constant.ApiPath;
import com.yurakim.readingtrace.auth.dto.LoginDto;
import com.yurakim.readingtrace.auth.dto.RegisterDto;
import com.yurakim.readingtrace.auth.service.IAuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(ApiPath.AUTH)
public class AuthController {

    private final IAuthService iAuthService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterDto registerDto) {
        String message = iAuthService.register(registerDto);
        return ResponseEntity.ok(message);
    }

    //TODO: add validation for loginDto
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDto loginDto) {
        String message = iAuthService.login(loginDto);
        return ResponseEntity.ok(message);
    }

}
