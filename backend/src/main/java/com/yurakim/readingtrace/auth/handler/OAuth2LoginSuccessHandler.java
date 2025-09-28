package com.yurakim.readingtrace.auth.handler;

import com.yurakim.readingtrace.auth.constant.JWT;
import com.yurakim.readingtrace.auth.security.UserDetailsImpl;
import com.yurakim.readingtrace.auth.service.JwtService;
import com.yurakim.readingtrace.user.entity.Role;
import com.yurakim.readingtrace.user.entity.User;
import com.yurakim.readingtrace.user.repository.RoleRepository;
import com.yurakim.readingtrace.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

//TODO: handle edge case where a user signs up with the same email via both OAuth2 and normal password signup.
//TODO: create a role mapping helper
//TODO: manage constants ROLE_USER, FRONTEND_URL in single source
@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final String ROLE_USER = "ROLE_USER";

    @Value("${frontend.url}")
    private String frontendUrl;
    private String FRONTEND_URL = frontendUrl+"/something";

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        if("google".equalsIgnoreCase(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())){
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
            String email = attributes.getOrDefault("email", "").toString();
            String idAttributeKey = "sub";

            Authentication newAuthentication;
            Optional<User> optExistingUser = userRepository.findByEmail(email);
            if(optExistingUser.isPresent()){
                User existingUser = optExistingUser.get();
                existingUser.setLastLoginAt(LocalDateTime.now());
                existingUser.setOAuth2Login(true);
                existingUser.setOAuth2Provider(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                userRepository.save(existingUser);

                UserDetailsImpl userDetails = new UserDetailsImpl(existingUser.getId(), existingUser.getEmail(), existingUser.getPassword(), existingUser.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet()));
                newAuthentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                String accessToken = jwtService.generateAccessToken(newAuthentication);
                String rawRefreshToken  = jwtService.generateRefreshToken(existingUser, null);

                response.addHeader(JWT.JWT_HEADER, JWT.JWT_PREFIX + accessToken);
                //TODO: refactor token setting code (repeating auth controller logic)
                //TODO: return user information
                Cookie refreshTokenCookie = new Cookie(JWT.REFRESH_TOKEN_COOKIE_NAME, rawRefreshToken);
                refreshTokenCookie.setHttpOnly(true);
                refreshTokenCookie.setPath("/");
                //refreshTokenCookie.setPath(ApiPath.AUTH+"/refresh");
                refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
                response.addCookie(refreshTokenCookie);
            }else{

                User newUser = new User();
                newUser.setEmail(email);
                newUser.setPassword("{noop}OAUTH2_USER");
                newUser.setLastLoginAt(LocalDateTime.now());
                newUser.setOAuth2Login(true);
                newUser.setOAuth2Provider(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                Set<Role> roles = new HashSet<>();
                roles.add(roleRepository.findByName(ROLE_USER).get());
                newUser.setRoles(roles);
                userRepository.save(newUser);

                UserDetailsImpl userDetails = new UserDetailsImpl(newUser.getId(), newUser.getEmail(), newUser.getPassword(), newUser.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet()));
                newAuthentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                String accessToken = jwtService.generateAccessToken(newAuthentication);
                String rawRefreshToken  = jwtService.generateRefreshToken(newUser, null);

                response.addHeader(JWT.JWT_HEADER, JWT.JWT_PREFIX + accessToken);
                //TODO: refactor token setting code (repeating auth controller logic)
                //TODO: return user information
                Cookie refreshTokenCookie = new Cookie(JWT.REFRESH_TOKEN_COOKIE_NAME, rawRefreshToken);
                refreshTokenCookie.setHttpOnly(true);
                refreshTokenCookie.setPath("/");
                //refreshTokenCookie.setPath(ApiPath.AUTH+"/refresh");
                refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
                response.addCookie(refreshTokenCookie);
            }

            CsrfToken csrfToken = csrfTokenRepository.generateToken(request);
            csrfTokenRepository.saveToken(csrfToken, request, response);
            response.setHeader("X-CSRF-TOKEN", csrfToken.getToken());

            SecurityContextHolder.getContext().setAuthentication(newAuthentication);
            getRedirectStrategy().sendRedirect(request, response, FRONTEND_URL);
            return;
        }
        throw new IllegalStateException("Unsupported OAuth2 provider: "
                + oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
    }
}