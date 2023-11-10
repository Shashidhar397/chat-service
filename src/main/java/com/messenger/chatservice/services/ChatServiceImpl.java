package com.messenger.chatservice.services;

import com.messenger.chatservice.models.AddUserModel;
import com.messenger.chatservice.models.AddUserResponseModel;
import com.messenger.chatservice.models.ChatMessageRequestModel;
import com.messenger.chatservice.models.ChatMessageResponseModel;
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
        this.chatSessionService.saveSessionDetails(simpMessageHeaderAccessor.getSessionId(), addUserModel.getUuid());
    }

    @Override
    public ChatMessageResponseModel sendMessage(ChatMessageRequestModel chatMessageRequestModel) {
        AddUserResponseModel toUser = this.chatSessionService.getSessionIdByUserUuid(chatMessageRequestModel.getRecipient().getUuid());
        System.out.println("/chat-service-private/"+toUser.getSessionId());
        ChatMessageResponseModel chatMessageResponseModel = new ChatMessageResponseModel(chatMessageRequestModel);
        this.simpMessagingTemplate.convertAndSend("/chat-service-private/"+toUser.getSessionId(), chatMessageResponseModel);
        return chatMessageResponseModel;
    }
}
