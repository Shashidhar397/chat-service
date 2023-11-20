package com.messenger.chatservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.messenger.chatservice.models.AddUserModel;
import com.messenger.chatservice.models.ChatHistoryResponseModel;
import com.messenger.chatservice.models.ChatMessageRequestModel;
import com.messenger.chatservice.models.ChatMessageResponseModel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public interface ChatService {

    void addUser(AddUserModel addUserModel, SimpMessageHeaderAccessor simpMessageHeaderAccessor);

    ChatMessageResponseModel sendMessage(ChatMessageRequestModel chatMessageRequestModel) throws JsonProcessingException;

    ChatHistoryResponseModel chatHistory(String userUuid);

    void processConsumedMessage(String chatMessageResponseModeStr) throws JsonProcessingException;

    void updateMessageStatusAndSendMessage(ChatMessageResponseModel chatMessageResponseModel) throws JsonProcessingException;
}
