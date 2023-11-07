package com.messenger.chatservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
@Slf4j
public class ChatServiceWebSocketHandler {

    @EventListener
    public void connectedHandler(SessionConnectedEvent event) {
        log.info("Its connected"+event.getMessage().getPayload().toString());
        String sessionId = (String) event.getMessage().getHeaders().get("simpSessionId");
        String name = (String) event.getMessage().getHeaders().get(StompHeaders.LOGIN);
        log.info(sessionId+" => "+name);
    }

}
