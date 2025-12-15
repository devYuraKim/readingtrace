package com.yurakim.readingtrace.chat.config;

import com.yurakim.readingtrace.auth.constant.JWT;
import com.yurakim.readingtrace.auth.security.UserDetailsImpl;
import com.yurakim.readingtrace.auth.service.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class StompConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtService jwtService;
    private final Environment environment;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins(environment.getProperty("frontend.url"));
                //.withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if(accessor == null) System.out.println("ACCESSOR IS NULL");
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    List<String> authHeaders = accessor.getNativeHeader(JWT.JWT_HEADER);
                    if (authHeaders != null && !authHeaders.isEmpty()) {
                        try {
                            String token = authHeaders.get(0).substring(JWT.JWT_PREFIX.length());
                            Authentication authentication = jwtService.validateAccessToken(token);
                            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                            // For convenience, we use 'userId' instead of 'email' as the principal name.
                            // By default, principal.getUsername() returns the email, but for STOMP routing, relying on the database to retrieve this information is unnecessary.
                            accessor.setUser(
                                    new UsernamePasswordAuthenticationToken(
                                            userDetails.getId().toString(),
                                            null,
                                            userDetails.getAuthorities()
                                    )
                            );
                            SecurityContextHolder.getContext()
                                    .setAuthentication(authentication);
                        } catch (JwtException e) {
                            // optional: log invalid token, but don't block connection
                            System.out.println("Invalid access token, connecting as guest");
                        }
                    }
                }
                return message;
            }
        });
    }

}
