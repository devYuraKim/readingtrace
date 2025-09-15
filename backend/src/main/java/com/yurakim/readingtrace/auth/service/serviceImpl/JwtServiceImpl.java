package com.yurakim.readingtrace.auth.service.serviceImpl;

import com.yurakim.readingtrace.auth.constant.JWT;
import com.yurakim.readingtrace.auth.entity.entity.RefreshToken;
import com.yurakim.readingtrace.auth.repository.RefreshTokenRepository;
import com.yurakim.readingtrace.auth.service.JwtService;
import com.yurakim.readingtrace.user.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class JwtServiceImpl implements JwtService {

    private final Environment environment;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public String generateJwt(Authentication authentication) {

        if(authentication!=null && authentication.isAuthenticated()){

            // Get JWT configuration from environment
            String secret = environment.getProperty(JWT.JWT_SECRET_KEY_PROPERTY);
            String issuer = environment.getProperty(JWT.JWT_ISSUER_PROPERTY);
            Long expiration = Duration.ofHours(Long.parseLong(environment.getProperty(JWT.JWT_EXPIRATION_PROPERTY))).toMillis();
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

            String email = authentication.getName();
            List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            Date now = new Date();
            Date expiry = new Date(now.getTime() + expiration);

            return Jwts.builder()
            .issuer(issuer)
            .subject(email)
            .claim("roles", roles)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(secretKey)
            .compact();
        }
        throw new IllegalStateException("No authenticated user found");
    }

    @Override
    public String generateAccessToken(Authentication authentication) {
        String secret = environment.getProperty(JWT.ACCESS_SECRET_KEY_PROPERTY);
        String issuer = environment.getProperty(JWT.JWT_ISSUER_PROPERTY);
        Long expiration = Duration.ofMinutes(Long.parseLong(environment.getProperty(JWT.ACCESS_EXPIRATION_MINUTES_PROPERTY))).toMillis();
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        String email = authentication.getName();
        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .issuer(issuer)
                .subject(email)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    //TODO: separate refresh token related logic to RefreshTokenService
    @Override
    public String generateRefreshToken(User user) {
        String secret = environment.getProperty(JWT.REFRESH_SECRET_KEY_PROPERTY);
        String issuer = environment.getProperty(JWT.JWT_ISSUER_PROPERTY);
        Long expiration = Duration.ofDays(Long.parseLong(environment.getProperty(JWT.REFRESH_EXPIRATION_DAYS_PROPERTY))).toMillis();
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        String refreshToken = Jwts.builder()
                .issuer(issuer)
                .subject(user.getEmail())
                .setId(UUID.randomUUID().toString())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();

        LocalDateTime expiryLocalDateTime = expiry.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        RefreshToken refreshTokenDB = new RefreshToken();
        refreshTokenDB.setTokenId(refreshToken);
        refreshTokenDB.setUserId(user.getId());
        refreshTokenDB.setExpiryDate(expiryLocalDateTime);
        refreshTokenRepository.save(refreshTokenDB);

        return refreshToken;
    }

}
