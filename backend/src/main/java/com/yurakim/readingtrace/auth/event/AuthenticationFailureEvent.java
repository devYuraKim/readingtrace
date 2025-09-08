package com.yurakim.readingtrace.auth.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.stereotype.Component;

//LOGGING FOR AUTHENTICATION FAILURE
@Slf4j
@Component
public class AuthenticationFailureEvent {

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failureEvent) {
        log.error("Authentication failed for user: {} due to: {}",
                failureEvent.getAuthentication().getName(),
                failureEvent.getException().getMessage());
    }
}