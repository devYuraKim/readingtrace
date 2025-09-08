package com.yurakim.readingtrace.auth.service;

import org.springframework.security.core.Authentication;

public interface JwtService {

    String generateJwt(Authentication authentication);

}
