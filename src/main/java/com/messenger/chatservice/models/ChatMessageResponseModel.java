package com.messenger.chatservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    public ChatMessageResponseModel(ChatMessageRequestModel chatMessageRequestModel, Map<String, User> users) {
        this.uuid = UUID.randomUUID().toString();
        this.content = chatMessageRequestModel.getContent();
        this.recipient = users.get(chatMessageRequestModel.getRecipientUuid());
        this.sender = users.get(chatMessageRequestModel.getSenderUuid());
    }
}
