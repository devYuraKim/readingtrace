package com.yurakim.readingtrace.auth.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AuthenticationProviderImpl implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        //step3.provider checks user details with UserDetailsServiceImpl and PasswordEncoder(SecurityConfig)
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // TODO: additional rules beyond username/password with a custom UserDetails implementation
        if(passwordEncoder.matches(password, userDetails.getPassword())) {
            //step4.provider returns authenticated object -> back to IAuthServiceImpl
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } else {
        throw new BadCredentialsException("Invalid credentials");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
