package com.messenger.chatservice.services;

import com.messenger.chatservice.models.AddUserModel;
import com.messenger.chatservice.models.ChatMessage;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public interface ChatService {

    void addUser(AddUserModel addUserModel, SimpMessageHeaderAccessor simpMessageHeaderAccessor);

    public void sendMessage(ChatMessage chatMessage);
}
