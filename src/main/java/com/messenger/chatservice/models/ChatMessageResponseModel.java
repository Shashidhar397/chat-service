package com.messenger.chatservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.messenger.chatservice.models.entity.Conversation;
import com.messenger.chatservice.models.entity.Message;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ChatMessageResponseModel {
    private String uuid;
    private String content;
    private User recipient;
    private User sender;
    private MessageType messageType;
    private String conversationUuid;

    public ChatMessageResponseModel(Message message, Map<String, User> users, Conversation conversation) {
        this.uuid = message.getUuid().toString();
        this.content = message.getContent();
        this.recipient = users.get(message.getRecipientUuid());
        this.sender = users.get(message.getSenderUuid());
        this.conversationUuid = conversation.getUuid().toString();
    }
}
