package com.messenger.chatservice.services;

import com.messenger.chatservice.models.*;
import com.messenger.chatservice.models.entity.Conversation;
import com.messenger.chatservice.models.entity.Message;
import com.messenger.chatservice.repository.ConversationRepository;
import com.messenger.chatservice.repository.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author shashidhar
 */
@Service
public class ChatServiceImpl implements ChatService{
    @Autowired
    private ChatSessionService chatSessionService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserService userService;

    @Override
    public void addUser(AddUserModel addUserModel, SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        this.chatSessionService.saveSessionDetails(simpMessageHeaderAccessor.getSessionId(), addUserModel.getUuid());
    }

    @Override
    @Transactional
    public ChatMessageResponseModel sendMessage(ChatMessageRequestModel chatMessageRequestModel) {
        AddUserResponseModel toUser = this.chatSessionService.getSessionIdByUserUuid(chatMessageRequestModel.getRecipientUuid());
        System.out.println("/chat-service-private/"+toUser.getSessionId());
        Map<String, User> usersMap = this.userService.getUsers(Arrays.asList(chatMessageRequestModel.getSenderUuid(), chatMessageRequestModel.getRecipientUuid()));

        Message message = this.messagesRepository.save(new Message(chatMessageRequestModel));
        Conversation conversation = null;
        System.out.println("conversation Id:"+chatMessageRequestModel.getConversationUuid());
        if(chatMessageRequestModel.getConversationUuid() == null){
            conversation = new Conversation(message);
            this.conversationRepository.save(conversation);
        } else {
            conversation = this.conversationRepository.findByUuid(UUID.fromString(chatMessageRequestModel.getConversationUuid()));
            conversation.getMessages().add(message.getUuid());
        }
        assert conversation != null;
        Conversation savedConversation = this.conversationRepository.save(conversation);
        ChatMessageResponseModel chatMessageResponseModel = new ChatMessageResponseModel(message, usersMap, savedConversation);
        this.simpMessagingTemplate.convertAndSend("/chat-service-private/"+toUser.getSessionId(), chatMessageResponseModel);
        return chatMessageResponseModel;
    }

    @Override
    public ChatHistoryResponseModel chatHistory(String userUuid) {
        ChatHistoryResponseModel chatHistoryResponseModel = new ChatHistoryResponseModel();
        List<Conversation> conversations = this.conversationRepository.findAllByUsersInvolvedContains(userUuid);
        List<String> usersUuid = new ArrayList<>();
        conversations.forEach(conversation -> {
            usersUuid.addAll(conversation.getUsersInvolved());
        });
        List<ConversationResponseModel> conversationResponseModels = new ArrayList<>();
        Map<String, User> usersMap = this.userService.getUsers(usersUuid);
        conversations.forEach(conversation -> {
            List<User> users = new ArrayList<>();
            conversation.getUsersInvolved().forEach(uuid -> {
                users.add(usersMap.get(uuid));
            });
            List<Message> messages = this.messagesRepository.findAllByMessageUuids(conversation.getMessages());
            messages.sort(Comparator.comparing(Message::getCreatedAt));
            List<ChatMessageResponseModel> chatMessageResponseModels = new ArrayList<>();
            messages.forEach(message -> {
                chatMessageResponseModels.add(new ChatMessageResponseModel(message, usersMap, conversation));
            });
            ConversationResponseModel conversationResponseModel = new ConversationResponseModel();
            conversationResponseModel.setUuid(conversation.getUuid().toString());
            conversationResponseModel.setParticipants(users);
            conversationResponseModel.setMessages(chatMessageResponseModels);
            conversationResponseModels.add(conversationResponseModel);
        });
        chatHistoryResponseModel.setConversations(conversationResponseModels);
        return chatHistoryResponseModel;
    }
}
