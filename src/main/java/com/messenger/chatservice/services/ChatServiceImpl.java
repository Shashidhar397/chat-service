package com.messenger.chatservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.messenger.chatservice.models.*;
import com.messenger.chatservice.models.entity.Conversation;
import com.messenger.chatservice.models.entity.Message;
import com.messenger.chatservice.repository.ConversationRepository;
import com.messenger.chatservice.repository.MessagesRepository;
import com.messenger.chatservice.utils.KafkaProducer;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * @author shashidhar
 */
@Service
public class ChatServiceImpl implements ChatService{
    private final ChatSessionService chatSessionService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessagesRepository messagesRepository;
    private final ConversationRepository conversationRepository;
    private final UserService userService;
    private final KafkaProducer<java.io.Serializable> kafkaProducer;
    private final ObjectMapper objectMapper;


    public ChatServiceImpl(ChatSessionService chatSessionService, SimpMessagingTemplate simpMessagingTemplate, MessagesRepository messagesRepository, ConversationRepository conversationRepository, UserService userService, KafkaProducer<Serializable> kafkaProducer, ObjectMapper objectMapper, ObjectMapper objectMapper1) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messagesRepository = messagesRepository;
        this.conversationRepository = conversationRepository;
        this.userService = userService;
        this.kafkaProducer = kafkaProducer;
        this.chatSessionService = chatSessionService;
        this.objectMapper = objectMapper1;
    }


    @Override
    public void addUser(AddUserModel addUserModel, SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        this.chatSessionService.saveSessionDetails(simpMessageHeaderAccessor.getSessionId(), addUserModel.getUuid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChatMessageResponseModel sendMessage(ChatMessageRequestModel chatMessageRequestModel) throws JsonProcessingException {
        ChatMessageResponseModel chatMessageResponseModel;
        Map<String, User> usersMap = this.userService.getUsers(Arrays.asList(chatMessageRequestModel.getSenderUuid(), chatMessageRequestModel.getRecipientUuid()));

        Message message = this.messagesRepository.save(new Message(chatMessageRequestModel));
        Conversation conversation;
        if(chatMessageRequestModel.getConversationUuid() == null){
            conversation = new Conversation(message);
            this.conversationRepository.save(conversation);
        } else {
            conversation = this.conversationRepository.findByUuid(UUID.fromString(chatMessageRequestModel.getConversationUuid()));
            conversation.getMessages().add(message.getUuid());
        }
        Conversation savedConversation = this.conversationRepository.save(conversation);
        chatMessageResponseModel = new ChatMessageResponseModel(message, usersMap, savedConversation);
        String messageStr = objectMapper.writeValueAsString(chatMessageResponseModel);
        System.out.println(messageStr);
        this.kafkaProducer.sendMessage(message.getUuid().toString(), messageStr);
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
            List<Message> messages = this.messagesRepository.findAllBySenderUuidInAndRecipientUuidIn(conversation.getUsersInvolved());
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

    @Override
    public void processConsumedMessage(String chatMessageResponseModeStr) throws JsonProcessingException {
        ChatMessageResponseModel chatMessageResponseModel = this.objectMapper.readValue(chatMessageResponseModeStr, ChatMessageResponseModel.class);
        switch (chatMessageResponseModel.getMessageStatus()) {
            case SEND -> {
                AddUserResponseModel toUser = this.chatSessionService.getSessionIdByUserUuid(chatMessageResponseModel.getRecipient().getUuid());
                System.out.println("/chat-service-private/" + toUser.getSessionId());
                if (toUser.getSessionId() != null && !toUser.getSessionId().isEmpty()) {
                    chatMessageResponseModel.setMessageStatus(MessageStatus.SENT);
                    this.simpMessagingTemplate.convertAndSend("/chat-service-private/" + toUser.getSessionId(), chatMessageResponseModel);
                    this.updateMessageStatus(chatMessageResponseModel);
                }
            }
            case DELIVERED, READ -> sendMessageUpdates(chatMessageResponseModel);
            default -> {
            }
        }
    }

    @Override
    public void updateMessageStatusAndSendMessage(ChatMessageResponseModel chatMessageResponseModel) throws JsonProcessingException {
        this.updateMessageStatus(chatMessageResponseModel);
        this.kafkaProducer.sendMessage(chatMessageResponseModel.getUuid(), this.objectMapper.writeValueAsString(chatMessageResponseModel));
    }

    private void updateMessageStatus(ChatMessageResponseModel chatMessageResponseModel) {
        this.messagesRepository.updateMessageStatusByUuid(UUID.fromString(chatMessageResponseModel.getUuid()), chatMessageResponseModel.getMessageStatus());
    }

    private void sendMessageUpdates(ChatMessageResponseModel chatMessageResponseModel) {
        AddUserResponseModel sender = this.chatSessionService.getSessionIdByUserUuid(chatMessageResponseModel.getSender().getUuid());
        if (sender.getSessionId() != null && !sender.getSessionId().isEmpty()) {
            this.simpMessagingTemplate.convertAndSend("/chat-service-private/"+sender.getSessionId()+"/message-updates", chatMessageResponseModel);
        }
    }

}
