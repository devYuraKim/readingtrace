package com.yurakim.readingtrace.auth.service.serviceImpl;

import com.yurakim.readingtrace.auth.constant.JWT;
import com.yurakim.readingtrace.auth.entity.entity.RefreshToken;
import com.yurakim.readingtrace.auth.exception.RefreshTokenException;
import com.yurakim.readingtrace.auth.repository.RefreshTokenRepository;
import com.yurakim.readingtrace.auth.service.JwtService;
import com.yurakim.readingtrace.user.entity.User;
import com.yurakim.readingtrace.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    private final UserRepository userRepository;

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
    public String generateRefreshToken(User user, LocalDateTime rotateExpiry) {
        String secret = environment.getProperty(JWT.REFRESH_SECRET_KEY_PROPERTY);
        String issuer = environment.getProperty(JWT.JWT_ISSUER_PROPERTY);
        Long expiration = Duration.ofDays(Long.parseLong(environment.getProperty(JWT.REFRESH_EXPIRATION_DAYS_PROPERTY))).toMillis();
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        Date now = new Date();
        Date expiry = rotateExpiry != null
                ? Date.from(rotateExpiry.atZone(ZoneId.systemDefault()).toInstant())
                : new Date(now.getTime() + expiration);

        String refreshToken = Jwts.builder()
                .issuer(issuer)
                .subject(user.getEmail())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .id(UUID.randomUUID().toString())
                .compact();

        LocalDateTime expiryLocalDateTime = expiry.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        RefreshToken refreshTokenDB = new RefreshToken();
        refreshTokenDB.setTokenHash(refreshToken);
        refreshTokenDB.setUserId(user.getId());
        refreshTokenDB.setExpiresAt(expiryLocalDateTime);
        refreshTokenRepository.save(refreshTokenDB);

        return refreshToken;
    }

    @Override
    public Boolean isValidRefreshToken(String refreshToken) {
        RefreshToken existing = refreshTokenRepository.findByTokenHash(refreshToken).orElseThrow(
                () -> new RefreshTokenException(JWT.INVALID_REFRESH_TOKEN, "Refresh token not found")
        );
        if(!existing.isRevoked() && !existing.getExpiresAt().isBefore(LocalDateTime.now())) {
            return true;
        }
        return false;
    }

    @Override
    public String getSubject(String refreshToken) {
        String secret = environment.getProperty(JWT.REFRESH_SECRET_KEY_PROPERTY);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(refreshToken).getPayload();
        return claims.getSubject();
    }
    //assert Jwts.parser().verifyWith(key).build().parseSignedClaims(jws).getPayload().getSubject().equals("Joe");

    @Override
    public String refreshAccessToken(String refreshToken) {
        if (!isValidRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }
        String userEmail = this.getSubject(refreshToken);
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userEmail, null, user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role.getName())).collect(Collectors.toList()));
        String newAccessToken = this.generateAccessToken(authentication);
        return newAccessToken;
    }

    @Override
    public RefreshToken validateRefreshToken(String refreshToken) {
        //1.Check DB
        RefreshToken existing = refreshTokenRepository.findByTokenHash(refreshToken).orElseThrow(
                () -> new RefreshTokenException(JWT.INVALID_REFRESH_TOKEN, "Refresh token not found in DB")
        );

        //2.Check revocation/expiry in DB
        if(existing.isRevoked()) {
            throw new RefreshTokenException(JWT.REVOKED_REFRESH_TOKEN, "Refresh token has been revoked");
        }
        if( existing.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new RefreshTokenException(JWT.EXPIRED_REFRESH_TOKEN, "Refresh token has expired");
        }

        //3.Verify JWT signature
        String secret = environment.getProperty(JWT.REFRESH_SECRET_KEY_PROPERTY);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(refreshToken);
        } catch (JwtException e) {
            throw new JwtException("Invalid refresh token: " + e.getMessage());
        }
        //TODO: validate user

        //4.Return existing refresh token
        return existing;
    }

    @Transactional
    @Override
    public String rotateRefreshToken(String oldRefreshToken) {
        //1. validate existing refresh token
        RefreshToken existingToken = this.validateRefreshToken(oldRefreshToken);
        //2. get original expiry
        LocalDateTime originalExpiry = existingToken.getExpiresAt();
        //TODO: validate user
        //3. get user
        String userEmail = this.getSubject(oldRefreshToken);
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        //4. generate new refresh token and revoke old one
        String newRefreshTokenStr = this.generateRefreshToken(user, originalExpiry);
        RefreshToken newRefreshToken = refreshTokenRepository.findByTokenHash(newRefreshTokenStr).orElseThrow(() -> new RefreshTokenException(JWT.INVALID_REFRESH_TOKEN, "Refresh token not found in DB"));
        existingToken.setRevoked(true);
        existingToken.setReplacedById(newRefreshToken.getId());
        refreshTokenRepository.save(existingToken);

        return newRefreshTokenStr;
    }

    @Deprecated
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

}
