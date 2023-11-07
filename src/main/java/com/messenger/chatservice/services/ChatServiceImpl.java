package com.messenger.chatservice.services;

import com.messenger.chatservice.models.AddUserModel;
import com.messenger.chatservice.models.AddUserResponseModel;
import com.messenger.chatservice.models.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService{
    @Autowired
    private ChatSessionService chatSessionService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void addUser(AddUserModel addUserModel, SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        this.chatSessionService.saveSessionDetails(simpMessageHeaderAccessor.getSessionId(), addUserModel.getUserName());
    }

    @Override
    public void sendMessage(ChatMessage chatMessage) {
        AddUserResponseModel toUser = this.chatSessionService.getSessionIdByUserName(chatMessage.getReceiver());
        this.simpMessagingTemplate.convertAndSend("/chat-service-private/"+toUser.getSessionId(), chatMessage);
    }
}
