package com.messenger.chatservice.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author shashidhar
 */
@Component
public class KafkaProducer<String> {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${spring.kafka.topic-name}")
    private String topicName;

    public void sendMessage(String key, String msg) {
        kafkaTemplate.send(java.lang.String.valueOf(topicName), key, msg);
    }
}
