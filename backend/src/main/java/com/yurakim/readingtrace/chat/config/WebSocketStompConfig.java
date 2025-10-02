package com.yurakim.readingtrace.chat.config;

import com.yurakim.readingtrace.auth.constant.JWT;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtService jwtService;
    private final Environment environment;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //expose a STOMP endpoint at /support
        registry.addEndpoint("/support")
                .setAllowedOrigins(environment.getProperty("frontend.url"));
        //TODO: check if the following setups are necessary
        //.setAllowedOrigins("*")
        //.withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // STOMP messages whose destination header begins with /app
        // are routed to @MessageMapping methods in @Controller classes
        registry.setApplicationDestinationPrefixes("/app");
        // Outgoing messages prefixed with /topic or /queue are handled by simple broker
        // For built-in simple broker, prefixes do not have special meaning
        // But external brokers supports different STOMP destinations and prefixes
        registry.enableSimpleBroker("/topic", "/queue");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    List<String> authHeaders = accessor.getNativeHeader(JWT.JWT_HEADER);
                    //TODO: implement JWT token validation for STOMP connections
                    if (authHeaders != null && !authHeaders.isEmpty()) {
                        try {
                            String token = authHeaders.get(0).substring(JWT.JWT_PREFIX.length());
                            Authentication authentication = jwtService.validateAccessToken(token);
                            accessor.setUser(authentication);
                            SecurityContextHolder.getContext().setAuthentication(authentication);
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
