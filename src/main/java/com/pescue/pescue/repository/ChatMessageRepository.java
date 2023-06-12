package com.pescue.pescue.repository;

import com.pescue.pescue.model.ChatMessage;
import com.pescue.pescue.model.constant.MessageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    long countBySenderIDAndRecipientIDAndStatus(@Param("senderID") String senderID, @Param("recipientID") String recipientId, @Param("status") MessageStatus status);
    List<ChatMessage> findAllByChatRoomID (@Param("chatRoomID") String chatRoomID);
    List<ChatMessage> findAllBySenderIDAndRecipientID (@Param("senderID") String senderID, @Param("recipientID") String recipientID);
}
