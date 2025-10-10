package com.yurakim.readingtrace.auth.handler;

import com.yurakim.readingtrace.auth.constant.JWT;
import com.yurakim.readingtrace.auth.service.JwtService;
import com.yurakim.readingtrace.user.entity.Role;
import com.yurakim.readingtrace.user.entity.User;
import com.yurakim.readingtrace.user.repository.RoleRepository;
import com.yurakim.readingtrace.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

//TODO: handle edge case where a user signs up with the same email via both OAuth2 and normal password signup.
//TODO: create a role mapping helper
//TODO: manage constants ROLE_USER, FRONTEND_URL in single source
@RequiredArgsConstructor
@Component
public class OldOldOAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Value("${frontend.url}")
    private String frontendUrl;
    private static final String ROLE_USER = "ROLE_USER";

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        if("google".equalsIgnoreCase(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())){
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
            String email = attributes.getOrDefault("email", "").toString();
            String idAttributeKey = "sub";

            Authentication updatedAuthentication = userRepository.findByEmail(email)
                    .map(user -> {
                        user.setLastLoginAt(LocalDateTime.now());
                        userRepository.save(user);
                        DefaultOAuth2User oauth2User = new DefaultOAuth2User(
                                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet()),
                                attributes,
                                idAttributeKey
                        );
                        return new OAuth2AuthenticationToken(
                                oauth2User,
                                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet()),
                                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()
                        );
                    })
                    .orElseGet(() -> {
                        User newUser = new User();
                        newUser.setEmail(email);
                        newUser.setPassword("{noop}OAUTH2_USER");
                        newUser.setOAuth2Provider(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                        Set<Role> roles = new HashSet<>();
                        roles.add(roleRepository.findByName(ROLE_USER).get());
                        newUser.setRoles(roles);
                        userRepository.save(newUser);

                        DefaultOAuth2User oauth2User = new DefaultOAuth2User(
                                newUser.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet()),
                                attributes,
                                idAttributeKey
                        );
                        return new OAuth2AuthenticationToken(
                                oauth2User,
                                newUser.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet()),
                                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()
                        );
                    });
            SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
            String jwt = jwtService.generateJwt(updatedAuthentication);
            response.addHeader(JWT.JWT_HEADER, JWT.JWT_PREFIX + jwt);
            getRedirectStrategy().sendRedirect(request, response, frontendUrl);
            return;
        }
        throw new IllegalStateException("Unsupported OAuth2 provider: "
                + oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
    }
}