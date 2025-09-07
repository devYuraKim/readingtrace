package com.yurakim.readingtrace.shared.security;

import com.yurakim.readingtrace.shared.constant.ApiPath;
import com.yurakim.readingtrace.shared.exception.AccessDeniedHandlerImpl;
import com.yurakim.readingtrace.shared.exception.AuthenticationEntryPointImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@AllArgsConstructor
@Configuration
public class SecurityConfig {

    private final AuthenticationEntryPointImpl authenticationEntryPointImpl;
    private final AccessDeniedHandlerImpl accessDeniedHandlerImpl;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.securityContext(context -> context
                .requireExplicitSave(false))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

        //CSRF
        http.csrf(csrf -> csrf
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                .ignoringRequestMatchers("/error", "/actuator/**")
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));

        //CORS
        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowCredentials(true);
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setMaxAge(3600L);
            return config;
        }));

        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers(ApiPath.AUTH+"/csrf",ApiPath.AUTH+"/register", ApiPath.AUTH+"/login", "/error", "/actuator/**").permitAll()
                .anyRequest().authenticated()
        );

        http.exceptionHandling(e -> e
                .authenticationEntryPoint(authenticationEntryPointImpl)
                .accessDeniedHandler(accessDeniedHandlerImpl)
        );

        http.logout(loc -> loc
                .logoutUrl(ApiPath.AUTH+"/logout")
                .logoutSuccessHandler((request, response, authentication)
                        -> { response.setStatus(HttpServletResponse.SC_OK);
                             response.getWriter().write("Logged out");
                        })
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID"));

        http.httpBasic(Customizer.withDefaults());

        return http.build();
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
