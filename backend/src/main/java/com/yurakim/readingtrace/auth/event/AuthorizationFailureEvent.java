package com.yurakim.readingtrace.auth.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

//LOGGING FOR AUTHORIZATION FAILURE
@Slf4j
@Component
public class AuthorizationFailureEvent {

    @EventListener
    public void onFailure(org.springframework.security.authorization.event.AuthorizationDeniedEvent failureEvent) {
        log.error("Authorization failed for user: {} due to: {}",
                failureEvent.getAuthentication().get().getPrincipal(),
                failureEvent.getAuthorizationDecision().toString());
    }
}