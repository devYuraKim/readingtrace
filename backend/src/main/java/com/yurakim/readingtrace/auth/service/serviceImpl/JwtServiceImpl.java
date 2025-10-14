package com.yurakim.readingtrace.auth.service.serviceImpl;

import com.yurakim.readingtrace.auth.constant.JWT;
import com.yurakim.readingtrace.auth.entity.entity.RefreshToken;
import com.yurakim.readingtrace.auth.enums.InvalidationCause;
import com.yurakim.readingtrace.auth.exception.AccessTokenException;
import com.yurakim.readingtrace.auth.exception.RefreshTokenException;
import com.yurakim.readingtrace.auth.repository.RefreshTokenRepository;
import com.yurakim.readingtrace.auth.security.UserDetailsImpl;
import com.yurakim.readingtrace.auth.service.JwtService;
import com.yurakim.readingtrace.auth.util.Sha256Hashing;
import com.yurakim.readingtrace.user.entity.User;
import com.yurakim.readingtrace.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
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

        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = principal.getId();
        String email = principal.getEmail();
        Set<String> roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .issuer(issuer)
                .subject(email)
                .claim("userId", userId)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    @Override
    public Authentication validateAccessToken(String accessToken) {
        String secret = environment.getProperty(JWT.ACCESS_SECRET_KEY_PROPERTY);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        try{
            //1.check signature
            //throws JwtException if gone wrong
            Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(accessToken).getPayload();
            //2.check expiration
            if(claims.getExpiration().before(new Date())){
                throw new AccessTokenException("EXPIRED", "Access token expired");
            }
            //3.check issuer
            if(!environment.getProperty(JWT.JWT_ISSUER_PROPERTY).equals(claims.getIssuer())){
                throw new AccessTokenException("INVALID_ISSUER", "Invalid issuer");
            }
            //Build Authentication
            String email = claims.getSubject();
            Number userIdNumber = (Number) claims.get("userId");
            Long userId = userIdNumber.longValue();
            List<String> roles = (List<String>) claims.get("roles");
            UserDetailsImpl userDetails = new UserDetailsImpl(userId, email, null, AuthorityUtils.createAuthorityList(roles));
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }catch(JwtException e){
            throw new JwtException("Invalid access token received", e);
        }
    }

    //TODO: separate refresh token related logic to RefreshTokenService
    @Override
    public String generateRefreshToken(User user, LocalDateTime rotateExpiry) {
        String secret = environment.getProperty(JWT.REFRESH_SECRET_KEY_PROPERTY);
        String issuer = environment.getProperty(JWT.JWT_ISSUER_PROPERTY);
//        Long expiration = 1_000*60L;
        Long expiration = Duration.ofDays(Long.parseLong(environment.getProperty(JWT.REFRESH_EXPIRATION_DAYS_PROPERTY))).toMillis();
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        Date now = new Date();
        Date expiry = rotateExpiry != null
                ? Date.from(rotateExpiry.atZone(ZoneId.systemDefault()).toInstant())
                : new Date(now.getTime() + expiration);

        String rawRefreshToken = Jwts.builder()
                .issuer(issuer)
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .id(UUID.randomUUID().toString())
                .compact();
        String hashedRefreshToken = Sha256Hashing.generateSHA256Hash(rawRefreshToken);

        LocalDateTime expiryLocalDateTime = expiry.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setTokenHash(hashedRefreshToken);
        newRefreshToken.setUserId(user.getId());
        newRefreshToken.setExpiresAt(expiryLocalDateTime);
        refreshTokenRepository.save(newRefreshToken);

        return rawRefreshToken;
    }

    @Override
    public String getSubject(String rawRefreshToken) {
        String secret = environment.getProperty(JWT.REFRESH_SECRET_KEY_PROPERTY);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(rawRefreshToken).getPayload();
        return claims.getSubject();
    }
    //assert Jwts.parser().verifyWith(key).build().parseSignedClaims(jws).getPayload().getSubject().equals("Joe");

    @Override
    public String refreshAccessToken(String rawRefreshToken) {
        if (validateRefreshToken(rawRefreshToken) == null) {
            throw new RuntimeException("Invalid refresh token");
        }
        String userEmail = this.getSubject(rawRefreshToken);
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        Long userId = user.getId();

        UserDetailsImpl userDetails = new UserDetailsImpl(userId, userEmail, null, user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role.getName())).collect(Collectors.toSet()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        String newAccessToken = this.generateAccessToken(authentication);
        return newAccessToken;
    }

    @Override
    public RefreshToken validateRefreshToken(String rawRefreshToken) {
        String hashedRefreshToken = Sha256Hashing.generateSHA256Hash(rawRefreshToken);
        //1.Check DB
        RefreshToken existingRecord = refreshTokenRepository.findByTokenHash(hashedRefreshToken).orElseThrow(
                () -> new RefreshTokenException(JWT.INVALID_REFRESH_TOKEN, "Refresh token not found in DB")
        );
        //2.Check revocation
        if(existingRecord.isRevoked()) {
            throw new RefreshTokenException(JWT.REVOKED_REFRESH_TOKEN, "Refresh token has been revoked due to: "+existingRecord.getInvalidationCause());
        }
        //3.Check expiry
        if(existingRecord.getExpiresAt().isBefore(LocalDateTime.now())){
            if(existingRecord.getInvalidationCause() == InvalidationCause.NONE) {
                existingRecord.setInvalidationCause(InvalidationCause.EXPIRED);
                refreshTokenRepository.save(existingRecord);
            }
            throw new RefreshTokenException(JWT.EXPIRED_REFRESH_TOKEN, "Refresh token has expired");
        }
        //3.Verify JWT signature
        String secret = environment.getProperty(JWT.REFRESH_SECRET_KEY_PROPERTY);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(rawRefreshToken);
        } catch (JwtException e) {
            existingRecord.setRevoked(true);
            existingRecord.setInvalidationCause(InvalidationCause.COMPROMISED);
            refreshTokenRepository.save(existingRecord);
            log.warn("Refresh token has been compromised: JWT signature invalid || userId: {}, refreshTokenId: {}, refreshToken: {}, timestamp: {}",
                    existingRecord.getUserId(), existingRecord.getId(), existingRecord.getTokenHash(), LocalDateTime.now());
            throw new RefreshTokenException(JWT.COMPROMISED_REFRESH_TOKEN, "Refresh token has been compromised");
        }
        //4.Verify hash
        if(!existingRecord.getTokenHash().equals(hashedRefreshToken)){
            existingRecord.setRevoked(true);
            existingRecord.setInvalidationCause(InvalidationCause.COMPROMISED);
            refreshTokenRepository.save(existingRecord);
            log.warn("Refresh token has been compromised: Hash mismatch || userId: {}, refreshTokenId: {}, refreshToken: {}, timestamp: {}",
                    existingRecord.getUserId(), existingRecord.getId(), existingRecord.getTokenHash(), LocalDateTime.now());
            throw new RefreshTokenException(JWT.COMPROMISED_REFRESH_TOKEN, "Refresh token has been compromised");
        }
        //TODO: validate user
        //4.Return existing refresh token
        return existingRecord;
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
        String newRawRefreshToken = this.generateRefreshToken(user, originalExpiry);
        String hashedRefreshToken = Sha256Hashing.generateSHA256Hash(newRawRefreshToken);
        RefreshToken newRefreshTokenRecord = refreshTokenRepository.findByTokenHash(hashedRefreshToken)
                .orElseThrow(() -> new RefreshTokenException(JWT.INVALID_REFRESH_TOKEN, "Refresh token not found in DB"));
        existingToken.setRevoked(true);
        existingToken.setInvalidationCause(InvalidationCause.ROTATED);
        existingToken.setReplacedById(newRefreshTokenRecord.getId());
        refreshTokenRepository.save(existingToken);

        return newRawRefreshToken;
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
            Set<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
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

    @Deprecated
    @Override
    public Boolean isValidRefreshToken(String rawRefreshToken) {
        String hashedRefreshToken = Sha256Hashing.generateSHA256Hash(rawRefreshToken);
        RefreshToken existingRecord = refreshTokenRepository.findByTokenHash(hashedRefreshToken).orElseThrow(
                () -> new RefreshTokenException(JWT.INVALID_REFRESH_TOKEN, "Refresh token not found")
        );
        if(existingRecord.isRevoked()){
            return false;
        }
        if(existingRecord.getExpiresAt().isBefore(LocalDateTime.now())) {
            existingRecord.setInvalidationCause(InvalidationCause.EXPIRED);
            refreshTokenRepository.save(existingRecord);
            return false;
        }
        return true;
    }

}
