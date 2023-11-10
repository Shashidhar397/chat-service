package com.messenger.chatservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class User {
    private String userName;
    private String email;
    private String uuid;
}
