package com.yurakim.readingtrace.auth.security;

import com.yurakim.readingtrace.auth.exception.AccessDeniedHandlerImpl;
import com.yurakim.readingtrace.auth.exception.AuthenticationEntryPointImpl;
import com.yurakim.readingtrace.auth.filter.JWTTokenValidatorFilter;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Slf4j
@AllArgsConstructor
@Configuration
public class SecurityConfig {

    private static final String[] PUBLIC_ENDPOINTS = {
            ApiPath.AUTH+"/csrf",
            ApiPath.AUTH+"/register",
            ApiPath.AUTH+"/login",
            ApiPath.BASE+"/error",
            ApiPath.BASE+"/actuator/health"
    };

    private static final String[] PROTECTED_ACTUATOR_ENDPOINTS = {
            ApiPath.BASE+"/actuator/info"
    };

    private final AuthenticationEntryPointImpl authenticationEntryPointImpl;
    private final AccessDeniedHandlerImpl accessDeniedHandlerImpl;

    //TODO: set HTTPS configuration
    //TODO: create a list for ignoreRequestMatchers
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //CSRF
        http.csrf(csrf -> csrf
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                .ignoringRequestMatchers(PUBLIC_ENDPOINTS)
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));

        //TODO: check if filter sequence matter here
        //JWT validation filter
        http.addFilterBefore(new JWTTokenValidatorFilter(), AuthorizationFilter.class);

        //CORS
        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowCredentials(true);
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setExposedHeaders(Collections.singletonList("Authorization"));
            config.setMaxAge(3600L);
            return config;
        }));

        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                .requestMatchers(PROTECTED_ACTUATOR_ENDPOINTS).hasRole("ADMIN")
                .anyRequest().authenticated()
        );

        http.exceptionHandling(e -> e
                .authenticationEntryPoint(authenticationEntryPointImpl)
                .accessDeniedHandler(accessDeniedHandlerImpl)
        );

        //TODO: remove session based logout
        http.logout(loc -> loc
                .logoutUrl(ApiPath.AUTH+"/logout")
                .logoutSuccessHandler((request, response, authentication)
                        -> { response.setStatus(HttpServletResponse.SC_OK);
                             response.getWriter().write("Logged out");
                        })
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID"));

        //TODO: remove filter listing
        //check enabled filters
        SecurityFilterChain chain = http.build();
        log.info("===================Security Filter Chain===================");
        chain.getFilters().forEach(filter ->
                log.info("=== Filter: {}", filter.getClass().getSimpleName()));

        return chain;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
