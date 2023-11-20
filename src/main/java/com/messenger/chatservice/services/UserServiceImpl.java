package com.messenger.chatservice.services;

import com.messenger.chatservice.models.GetUsersRequestModel;
import com.messenger.chatservice.models.GetUsersResponseModel;
import com.messenger.chatservice.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author shashidhar
 */
@Service
public class UserServiceImpl implements UserService{

    private final RestTemplate restTemplate;

    public UserServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Map<String, User> getUsers(List<String> userUuids) {
        ResponseEntity<GetUsersResponseModel> response = restTemplate.postForEntity("http://localhost:8082/users/getUsers", new GetUsersRequestModel(userUuids), GetUsersResponseModel.class);
        if(response.getStatusCode().is2xxSuccessful()){
            return Objects.requireNonNull(response.getBody()).getUsers();
        }
        return null;
    }
}
