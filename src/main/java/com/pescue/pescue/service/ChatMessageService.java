package com.pescue.pescue.service;

import com.pescue.pescue.dto.MessageDTO;
import com.pescue.pescue.exception.UserNotFoundException;
import com.pescue.pescue.model.ChatMessage;
import com.pescue.pescue.model.MessageStatus;
import com.pescue.pescue.model.User;
import com.pescue.pescue.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final UserService userService;

    public ChatMessage save(MessageDTO dto){
        log.info("save messages");
        ChatMessage message = new ChatMessage();
        User sender = userService.findUserByID(dto.getSenderID());
        User recipient = userService.findUserByID(dto.getRecipientID());

        if (sender == null || recipient == null)
            return null;

        message.setSender(sender);
        message.setRecipient(recipient);
        message.setTimestamp(new Date(System.currentTimeMillis()));
        message.setContent(dto.getContent());
        message.setStatus(MessageStatus.RECEIVED);

        repository.save(message);
        return message;
    }

    public long countNewMessage(String senderId, String recipientId){
        return repository.countBySenderAndRecipientAndStatus(senderId, recipientId, MessageStatus.RECEIVED);
    }
    public ChatMessage findById(String id){
        return repository
                .findById(id)
                .map(chatMessage -> {
                    chatMessage.setStatus(MessageStatus.DELIVERED);
                    return repository.save(chatMessage);
                })
                .orElseThrow();
    }
//
//    private void updateStatuses(String senderId, String recipientId, MessageStatus status) {
//        Query query = new Query(
//                Criteria
//                        .where("senderId").is(senderId)
//                        .and("recipientId").is(recipientId));
//        Update update = Update.update("status", status);
//        mongoOperations.updateMulti(query, update, ChatMessage.class);
//    }
}
