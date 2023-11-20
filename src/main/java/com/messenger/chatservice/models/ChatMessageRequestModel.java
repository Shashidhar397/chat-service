package com.messenger.chatservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ChatMessageRequestModel {
    private String content;
    private String recipientUuid;
    private String senderUuid;
    private MessageType messageType;
    private MessageStatus messageStatus;
    private String conversationUuid;
}
