package com.messenger.chatservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.messenger.chatservice.services.ChatService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author shashidhar
 */
@Component
public class MessageListener {

    private final ChatService chatService;

    public MessageListener(ChatService chatService) {
        this.chatService = chatService;
    }

    @KafkaListener(topics = "${spring.kafka.topic-name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenToChatEvents(String message) {
        try {
            System.out.println("Message received: " + message);
            chatService.processConsumedMessage(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
