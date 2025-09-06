package com.yurakim.readingtrace.shared.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        LocalDateTime timestamp = LocalDateTime.now();
        int status = HttpStatus.FORBIDDEN.value();
        String message = (accessDeniedException != null && accessDeniedException.getMessage() != null) ? accessDeniedException.getMessage() : "Authorization failed";
        String path = request.getRequestURI();

        response.setHeader("Readtrace-Custom-Header", "Authorization failed");
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String responseBody = String.format("{\"timestamp\": \"%s\", \"status\": \"%d\", \"error\": \"%s\", \"path\": \"%s\"}", timestamp, status, message, path);
        response.getWriter().write(responseBody);

        }
}
