package com.yurakim.readingtrace.shared.config;

import com.yurakim.readingtrace.shared.constant.ApiPath;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers(ApiPath.AUTH+"/register", ApiPath.AUTH+"/login", "/error", "/actuator/**").permitAll()
                .anyRequest().authenticated()
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
