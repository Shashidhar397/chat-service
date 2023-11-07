package com.messenger.chatservice.controller;

import com.messenger.chatservice.models.AddUserModel;
import com.messenger.chatservice.models.AddUserResponseModel;
import com.messenger.chatservice.models.ChatMessage;
import com.messenger.chatservice.services.ChatService;
import com.messenger.chatservice.services.ChatSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Slf4j
public class ChatController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload AddUserModel addUserModel, SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        log.info("Adding user");
        this.chatService.addUser(addUserModel,simpMessageHeaderAccessor);
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        log.info(chatMessage.getContent());
        this.chatService.sendMessage(chatMessage);
    }

}
