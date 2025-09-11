package com.yurakim.readingtrace.auth.filter;

import com.yurakim.readingtrace.shared.constant.ApiPath;
import com.yurakim.readingtrace.auth.constant.JWT;
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
public class JWTTokenValidatorFilter extends OncePerRequestFilter {

    private final Environment environment;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = JWT.JWT_HEADER;
        String prefix = JWT.JWT_PREFIX;
        String secret = environment.getProperty(JWT.JWT_SECRET_KEY_PROPERTY, JWT.JWT_SECRET_KEY_DEFAULT_VALUE);

        if(header != null && header.startsWith(prefix)){
            String jwt = header.substring(prefix.length());
            try{
                Environment env = getEnvironment();
                if(env != null){
                    SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                    if(secretKey != null){
                        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt).getPayload();
                        String email = claims.getSubject();
                        List<String> roles = (List<String>) claims.get("roles");
                        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, AuthorityUtils.createAuthorityList(roles));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }catch(Exception e){
                throw new BadCredentialsException("Invalid Token received");
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        //exclude login
        return request.getServletPath().equals(ApiPath.AUTH+"/login");
    }
}
