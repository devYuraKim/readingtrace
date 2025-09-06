package com.yurakim.readingtrace.auth.event;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEvent {

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent successEvent){

    }

}
