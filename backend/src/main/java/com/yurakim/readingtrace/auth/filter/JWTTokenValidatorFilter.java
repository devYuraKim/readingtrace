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

//TODO: properly manage secret key
@Component
public class JWTTokenValidatorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(JWT.JWT_HEADER);
        if(header != null && header.startsWith(JWT.JWT_PREFIX)){
            String jwt = header.substring(JWT.JWT_PREFIX.length());
            try{
                Environment env = getEnvironment();
                if(env != null){
                    String secret = env.getProperty(JWT.JWT_SECRET_KEY, JWT.JWT_SECRET_KEY_DEFAULT_VALUE);
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
