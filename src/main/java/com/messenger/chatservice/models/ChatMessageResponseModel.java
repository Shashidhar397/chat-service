package com.messenger.chatservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ChatMessageResponseModel {
    private String uuid;
    private String content;
    private User recipient;
    private User sender;

    public ChatMessageResponseModel(ChatMessageRequestModel chatMessageRequestModel) {
        this.uuid = UUID.randomUUID().toString();
        this.content = chatMessageRequestModel.getContent();
        this.recipient = chatMessageRequestModel.getRecipient();
        this.sender = chatMessageRequestModel.getSender();
    }
}
