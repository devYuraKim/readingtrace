package com.yurakim.readingtrace.auth.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

//RESPONSE FOR AUTHENTICATION FAILURE
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        LocalDateTime timestamp = LocalDateTime.now();
        int status = HttpStatus.UNAUTHORIZED.value();
        String message = (authException != null && authException.getMessage() != null) ? authException.getMessage() : "Authentication failed";
        String path = request.getRequestURI();

        response.setHeader("Readtrace-Custom-Header", "Authentication failed");
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String responseBody = String.format("{\"timestamp\": \"%s\", \"status\": \"%d\", \"error\": \"%s\", \"path\": \"%s\"}", timestamp, status, message, path);
        response.getWriter().write(responseBody);

    }
}
