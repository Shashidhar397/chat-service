package com.messenger.chatservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.messenger.chatservice.models.AddUserModel;
import com.messenger.chatservice.models.ChatHistoryResponseModel;
import com.messenger.chatservice.models.ChatMessageRequestModel;
import com.messenger.chatservice.models.ChatMessageResponseModel;
import com.messenger.chatservice.services.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/updateMessageStatus", consumes = "application/json", produces = "application/json")
    public void updateMessageStatus(@RequestBody ChatMessageResponseModel chatMessageResponseModel) throws JsonProcessingException {
        log.info("updating message status");
        this.chatService.updateMessageStatusAndSendMessage(chatMessageResponseModel);
    }

    @PostMapping(value = "/sendMessage", consumes = "application/json", produces = "application/json")
    public ChatMessageResponseModel sendMessage(@RequestBody ChatMessageRequestModel chatMessageRequestModel) {
        log.info(chatMessageRequestModel.getContent());
        try {
            return this.chatService.sendMessage(chatMessageRequestModel);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/chatHistory/{userUuid}", produces = "application/json")
    public ChatHistoryResponseModel chatHistory(@PathVariable("userUuid") String userUuid) {
        return this.chatService.chatHistory(userUuid);
    }

}
