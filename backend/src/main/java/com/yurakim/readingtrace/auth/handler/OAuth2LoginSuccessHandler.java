package com.yurakim.readingtrace.auth.handler;

import com.yurakim.readingtrace.auth.constant.JWT;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

//TODO: handle edge case where a user signs up with the same email via both OAuth2 and normal password signup.
//TODO: create a role mapping helper
//TODO: manage constants ROLE_USER, FRONTEND_URL in single source
@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final String ROLE_USER = "ROLE_USER";

    @Value("${frontend.url}")
    private String frontendUrl;

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

            Optional<User> optExistingUser = userRepository.findByEmail(email);
            User user;

            if(optExistingUser.isPresent()){
                //existing user
                user = optExistingUser.get();
                user.setLastLoginAt(LocalDateTime.now());
                user.setOAuth2Login(true);
                user.setOAuth2Provider(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
            }else{
                //new user
                user = new User();
                user.setEmail(email);
                user.setPassword("{noop}OAUTH2_USER");
                user.setLastLoginAt(LocalDateTime.now());
                user.setOAuth2Login(true);
                user.setOAuth2Provider(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                Set<Role> roles = new HashSet<>();
                roles.add(roleRepository.findByName(ROLE_USER).get());
                user.setRoles(roles);
            }
            userRepository.save(user);

            String rawRefreshToken  = jwtService.generateRefreshToken(user, null);
            Cookie refreshTokenCookie = new Cookie(JWT.REFRESH_TOKEN_COOKIE_NAME, rawRefreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setPath("/");
            //refreshTokenCookie.setPath(ApiPath.AUTH+"/refresh");
            refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(refreshTokenCookie);

            getRedirectStrategy().sendRedirect(request, response, frontendUrl+"/oauth2/callback");
            return;
        }
        throw new IllegalStateException("Unsupported OAuth2 provider: "
                + oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
    }
}