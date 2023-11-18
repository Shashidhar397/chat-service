package com.messenger.chatservice.services;

import com.messenger.chatservice.models.AddUserModel;
import com.messenger.chatservice.models.ChatHistoryResponseModel;
import com.messenger.chatservice.models.ChatMessageRequestModel;
import com.messenger.chatservice.models.ChatMessageResponseModel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public interface ChatService {

    void addUser(AddUserModel addUserModel, SimpMessageHeaderAccessor simpMessageHeaderAccessor);

    public ChatMessageResponseModel sendMessage(ChatMessageRequestModel chatMessageRequestModel);

    public ChatHistoryResponseModel chatHistory(String userUuid);
}
