package com.messenger.chatservice.services;

import com.messenger.chatservice.models.User;

import java.util.List;
import java.util.Map;

/**
 * @author shashidhar
 */
public interface UserService {

    public Map<String, User> getUsers(List<String> userUuids);
}
