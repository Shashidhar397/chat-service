package com.messenger.chatservice.controller;

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
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload AddUserModel addUserModel, SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        log.info("Adding user");
        this.chatService.addUser(addUserModel,simpMessageHeaderAccessor);
    }

    @PostMapping(value = "/sendMessage", consumes = "application/json", produces = "application/json")
    public ChatMessageResponseModel sendMessage(@RequestBody ChatMessageRequestModel chatMessageRequestModel) {
        log.info(chatMessageRequestModel.getContent());
        return this.chatService.sendMessage(chatMessageRequestModel);
    }

    @GetMapping(value = "/chatHistory/{userUuid}", produces = "application/json")
    public ChatHistoryResponseModel chatHistory(@PathVariable("userUuid") String userUuid) {
        return this.chatService.chatHistory(userUuid);
    }

}
