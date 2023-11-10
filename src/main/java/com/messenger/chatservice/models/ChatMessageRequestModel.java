package com.messenger.chatservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ChatMessageRequestModel {
    private String content;
    private User recipient;
    private User sender;
}
