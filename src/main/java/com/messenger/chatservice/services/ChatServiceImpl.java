package com.messenger.chatservice.services;

import com.messenger.chatservice.MessagesRepository;
import com.messenger.chatservice.models.*;
import com.messenger.chatservice.models.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

@Service
public class ChatServiceImpl implements ChatService{
    @Autowired
    private ChatSessionService chatSessionService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    private UserService userService;

    @Override
    public void addUser(AddUserModel addUserModel, SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        this.chatSessionService.saveSessionDetails(simpMessageHeaderAccessor.getSessionId(), addUserModel.getUuid());
    }

    @Override
    public ChatMessageResponseModel sendMessage(ChatMessageRequestModel chatMessageRequestModel) {
        AddUserResponseModel toUser = this.chatSessionService.getSessionIdByUserUuid(chatMessageRequestModel.getRecipientUuid());
        System.out.println("/chat-service-private/"+toUser.getSessionId());
        Map<String, User> usersMap = this.userService.getUsers(Arrays.asList(chatMessageRequestModel.getSenderUuid(), chatMessageRequestModel.getRecipientUuid()));
        ChatMessageResponseModel chatMessageResponseModel = new ChatMessageResponseModel(chatMessageRequestModel, usersMap);
        this.messagesRepository.save(new Message(chatMessageRequestModel));
        this.simpMessagingTemplate.convertAndSend("/chat-service-private/"+toUser.getSessionId(), chatMessageResponseModel);
        return chatMessageResponseModel;
    }
}
