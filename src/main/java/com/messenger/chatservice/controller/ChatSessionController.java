package com.messenger.chatservice.controller;

import com.messenger.chatservice.models.AddUserResponseModel;
import com.messenger.chatservice.services.ChatSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatSessionController {

    @Autowired
    private ChatSessionService chatSessionService;

    @GetMapping("/getSession/{userName}")
    public ResponseEntity<AddUserResponseModel> getSession(@PathVariable String userName) {
        AddUserResponseModel addUserResponseModel = chatSessionService.getSessionIdByUserName(userName);
        return new ResponseEntity<>(addUserResponseModel, HttpStatus.OK);
    }

}
