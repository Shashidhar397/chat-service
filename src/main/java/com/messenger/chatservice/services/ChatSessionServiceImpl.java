package com.messenger.chatservice.services;

import com.messenger.chatservice.models.AddUserResponseModel;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatSessionServiceImpl implements ChatSessionService {

    public static Map<String, String> userToSessionMap = new HashMap<>();


    @Override
    public void saveSessionDetails(String sessionId, String userName) {
        userToSessionMap.put(userName, sessionId);
    }

    @Override
    public AddUserResponseModel getSessionIdByUserName(String userName) {
        return new AddUserResponseModel(userToSessionMap.get(userName), userName);
    }
}
