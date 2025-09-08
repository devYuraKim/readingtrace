package com.yurakim.readingtrace.auth.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthorizationEvent {

    @EventListener
    public void onFailure(AuthorizationDeniedEvent deniedEvent) {
        log.error("Authorization failed for user: {} due to: {}",
                deniedEvent.getAuthentication().get().getPrincipal(),
                deniedEvent.getAuthorizationDecision().toString());
    }
}