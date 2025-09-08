package com.yurakim.readingtrace.auth.service.serviceImpl;

import com.yurakim.readingtrace.auth.constant.JWT;
import com.yurakim.readingtrace.auth.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class JwtServiceImpl implements JwtService {

    private final Environment environment;

    @Override
    public String generateJwt(Authentication authentication) {

        if(authentication!=null && authentication.isAuthenticated()){
            String secret = environment.getProperty(JWT.JWT_SECRET_KEY, JWT.JWT_SECRET_KEY_DEFAULT_VALUE);
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

            String email = authentication.getName();
            List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            Date now = new Date();
            Date expiry = new Date(now.getTime() + JWT.JWT_EXPIRATION);

            return Jwts.builder()
            .issuer(JWT.JWT_ISSUER)
            .subject(email)
            .claim("roles", roles)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(secretKey)
            .compact();
        }
        throw new IllegalStateException("No authenticated user found");
    }

}
