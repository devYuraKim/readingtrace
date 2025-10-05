package com.yurakim.readingtrace.auth.filter;

import com.yurakim.readingtrace.auth.constant.JWT;
import com.yurakim.readingtrace.auth.exception.AccessTokenException;
import com.yurakim.readingtrace.auth.service.JwtService;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class AccessTokenValidatorFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String headerValue = request.getHeader(JWT.JWT_HEADER);

        if (headerValue != null && headerValue.startsWith(JWT.JWT_PREFIX)) {
            String accessToken = headerValue.substring(JWT.JWT_PREFIX.length());
            try {
                Authentication authentication = jwtService.validateAccessToken(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (AccessTokenException e){
                if ("EXPIRED".equals(e.getCode())) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //throw 401
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
                    return; // Don't continue the filter chain
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Authentication failed\"}");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        //exclude login and reset password
        return request.getServletPath().equals(ApiPath.AUTH+"/login")
            || request.getServletPath().equals(ApiPath.AUTH+"/forgot-password")
            || request.getServletPath().equals(ApiPath.AUTH+"/signup")
            || request.getServletPath().equals(ApiPath.AUTH+"/reset-password")
            || request.getServletPath().equals(ApiPath.AUTH+"/csrf")
            || request.getServletPath().equals(ApiPath.AUTH+"/refresh");
    }
}
