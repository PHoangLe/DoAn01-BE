package com.pescue.pescue.repository;

import com.pescue.pescue.model.ChatMessage;
import com.pescue.pescue.model.MessageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    long countBySenderAndRecipientAndStatus(@Param("sender.userID") String senderId, @Param("recipient.userID") String recipientId, @Param("status") MessageStatus status);
    List<ChatMessage> findChatMessageByRecipientOrSender (@Param("sender.userID") String senderId, @Param("recipient.userID") String recipientId);
}
