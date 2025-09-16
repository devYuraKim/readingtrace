package com.yurakim.readingtrace.auth.controller;

import com.yurakim.readingtrace.auth.constant.JWT;
import com.yurakim.readingtrace.auth.dto.AccessRefreshDto;
import com.yurakim.readingtrace.auth.dto.LoginRequestDto;
import com.yurakim.readingtrace.auth.dto.PasswordResetDto;
import com.yurakim.readingtrace.auth.dto.SignupDto;
import com.yurakim.readingtrace.auth.service.AuthService;
import com.yurakim.readingtrace.auth.service.JwtService;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import com.yurakim.readingtrace.user.dto.AuthenticatedUserDto;
import com.yurakim.readingtrace.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(ApiPath.AUTH)
public class AuthController {

    private final AuthService authService;
    @Lazy
    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping("/csrf")
    public ResponseEntity<Void> getCsrfToken() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignupDto signupDto) {
        String message = authService.signup(signupDto);
        return ResponseEntity.ok(message);
    }

    //TODO: fix return type
    //TODO: add validation for loginDto
        @PostMapping("/login")
        public ResponseEntity<AuthenticatedUserDto> loginUser(@RequestBody LoginRequestDto loginDto, HttpServletResponse response) {
            AccessRefreshDto tokens = authService.login(loginDto);

            //Issue AccessToken and RefreshToken
            String accessToken = tokens.getAccessToken();
            String refreshToken = tokens.getRefreshToken();

            Cookie refreshTokenCookie = new Cookie(JWT.REFRESH_TOKEN_COOKIE_NAME, refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setPath("/");
    //        refreshTokenCookie.setPath(ApiPath.AUTH+"/refresh");
            refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(refreshTokenCookie);

            //Return user info
            AuthenticatedUserDto user = userService.getAuthenticatedUser(loginDto.getEmail());

            return ResponseEntity.ok().header(JWT.JWT_HEADER, JWT.JWT_PREFIX + accessToken).body(user);
        }

        @PostMapping("/refresh")
        public ResponseEntity<Void> refreshToken(@CookieValue(JWT.REFRESH_TOKEN_COOKIE_NAME) String refreshToken, HttpServletResponse response){
            if (refreshToken == null || !jwtService.isValidRefreshToken(refreshToken)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String newAccessToken = jwtService.refreshAccessToken(refreshToken);
            String newRefreshToken = jwtService.rotateRefreshToken(refreshToken);

            Cookie refreshTokenCookie = new Cookie(JWT.REFRESH_TOKEN_COOKIE_NAME, newRefreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setPath("/");
    //        refreshTokenCookie.setPath(ApiPath.AUTH+"/refresh");
            refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(refreshTokenCookie);

            return ResponseEntity.ok().header(JWT.JWT_HEADER, JWT.JWT_PREFIX + newAccessToken).build();
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
