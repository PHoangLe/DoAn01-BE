package com.pescue.pescue.repository;

import com.pescue.pescue.model.ChatMessage;
import com.pescue.pescue.model.MessageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    long countBySenderIdAndRecipientIdAndStatus(@Param("senderId") String senderId, @Param("recipientId") String recipientId, @Param("status") MessageStatus status);

    List<ChatMessage> findByChatId(@Param("chatId") String chatId);
}
