package com.messenger.chatservice.services;

import com.messenger.chatservice.models.AddUserResponseModel;

public interface ChatSessionService {

    public void saveSessionDetails(String sessionId, String userName);

    public AddUserResponseModel getSessionIdByUserUuid(String userName);

}
