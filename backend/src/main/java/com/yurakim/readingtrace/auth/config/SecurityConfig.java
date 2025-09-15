package com.yurakim.readingtrace.auth.config;

import com.yurakim.readingtrace.auth.filter.JWTValidatorFilter;
import com.yurakim.readingtrace.auth.handler.AccessDeniedHandlerImpl;
import com.yurakim.readingtrace.auth.handler.AuthenticationEntryPointImpl;
import com.yurakim.readingtrace.auth.handler.OAuth2LoginSuccessHandler;
import com.yurakim.readingtrace.auth.security.AuthenticationProviderImpl;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Slf4j
@AllArgsConstructor
@Configuration
public class SecurityConfig {

    private static final String[] PUBLIC_ENDPOINTS = {
            ApiPath.AUTH+"/csrf",
            ApiPath.AUTH+"/signup",
            ApiPath.AUTH+"/login",
            ApiPath.AUTH+"/forgot-password",
            ApiPath.AUTH+"/reset-password",
            "/error",
            "/actuator/health",
            "/oauth2/**",
    };

    private static final String[] PROTECTED_ENDPOINTS = {
            ApiPath.ADMIN+"/**",
            "/actuator/info",
    };

    private final Environment environment;
    private final AuthenticationEntryPointImpl authenticationEntryPointImpl;
    private final AccessDeniedHandlerImpl accessDeniedHandlerImpl;
    private UserDetailsService userDetailsService;
    @Lazy
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    //TODO: set HTTPS configuration
    //TODO: create a list for ignoreRequestMatchers
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider());

        //CSRF
        http.csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));

        //TODO: check if filter sequence matter here
        //JWT validation filter
        http.addFilterBefore(new JWTValidatorFilter(environment), AuthorizationFilter.class);

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
                .requestMatchers(PROTECTED_ENDPOINTS).hasRole("ADMIN")
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                .anyRequest().authenticated())
                .oauth2Login(oa2 -> oa2.successHandler(oAuth2LoginSuccessHandler));

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
    public AuthenticationProviderImpl authenticationProvider() {
        return new AuthenticationProviderImpl(userDetailsService, passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration google = googleClientRegistration();
        return new InMemoryClientRegistrationRepository(google);
    }

    private ClientRegistration googleClientRegistration() {
        return CommonOAuth2Provider.GOOGLE.getBuilder("google")
                .clientId(environment.getProperty("oauth2.google.client-id"))
                .clientSecret(environment.getProperty("oauth2.google.client-secret")).build();
    }

}
