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
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final UserService userService;
    public ChatMessage save(MessageDTO dto, String chatRoomID){
        log.info("save messages");
        ChatMessage message = new ChatMessage();
        User sender = userService.findUserByID(dto.getSenderID());
        User recipient = userService.findUserByID(dto.getRecipientID());

        if (sender == null || recipient == null)
            return null;

        message.setChatRoomID(chatRoomID);
        message.setSenderID(dto.getSenderID());
        message.setRecipientID(dto.getRecipientID());
        message.setTimestamp(new Date(System.currentTimeMillis()));
        message.setContent(dto.getContent());
        message.setStatus(MessageStatus.DELIVERED);

        repository.save(message);
        return message;
    }

    public long countNewMessage(String senderId, String recipientId){
        return repository.countBySenderIDAndRecipientIDAndStatus(senderId, recipientId, MessageStatus.DELIVERED);
    }
}
