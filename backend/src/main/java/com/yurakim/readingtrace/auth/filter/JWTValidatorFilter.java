package com.yurakim.readingtrace.auth.filter;

import com.yurakim.readingtrace.auth.constant.JWT;
import com.yurakim.readingtrace.auth.security.UserDetailsImpl;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JWTValidatorFilter extends OncePerRequestFilter {

    private final Environment environment;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = JWT.JWT_HEADER;
        String prefix = JWT.JWT_PREFIX;
        String headerValue = request.getHeader(header);
        String secret = environment.getProperty(JWT.ACCESS_SECRET_KEY_PROPERTY);

        if (headerValue != null && headerValue.startsWith(prefix)) {
            String accessToken = headerValue.substring(prefix.length());
            try {
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                if (secretKey != null) {
                    Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(accessToken).getPayload();
                    String email = claims.getSubject();
                    Number userIdNumber = (Number) claims.get("userId");
                    Long userId = userIdNumber.longValue();
                    List<String> roles = (List<String>) claims.get("roles");

                    UserDetailsImpl userDetails = new UserDetailsImpl(userId, email, null, AuthorityUtils.createAuthorityList(roles));
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                throw new BadCredentialsException("Invalid token received: ", e);
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
