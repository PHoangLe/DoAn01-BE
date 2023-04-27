package com.pescue.pescue.service;

import com.pescue.pescue.model.ChatMessage;
import com.pescue.pescue.model.MessageStatus;
import com.pescue.pescue.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;
    private final MongoOperations mongoOperations;

    public ChatMessage save(ChatMessage message){
        log.info("save messages");
        message.setStatus(MessageStatus.RECEIVED);
        repository.save(message);
        return message;
    }

    public long countNewMessage(String senderId, String recipientId){
        return repository.countBySenderIdAndRecipientIdAndStatus(senderId, recipientId, MessageStatus.RECEIVED);
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId){
        var chatId = chatRoomService.getChatId(senderId, recipientId, false);

        var messages = chatId.map(repository::findByChatId).orElse(new ArrayList<>());

        if (messages.size() > 0)
            updateStatuses(senderId, recipientId, MessageStatus.DELIVERED);

        return messages;
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

    private void updateStatuses(String senderId, String recipientId, MessageStatus status) {
        Query query = new Query(
                Criteria
                        .where("senderId").is(senderId)
                        .and("recipientId").is(recipientId));
        Update update = Update.update("status", status);
        mongoOperations.updateMulti(query, update, ChatMessage.class);
    }
}
