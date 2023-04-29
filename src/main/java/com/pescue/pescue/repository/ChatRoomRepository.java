package com.pescue.pescue.repository;

import com.pescue.pescue.model.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    Optional<ChatRoom> findBySenderIdAndRecipientId(@Param("senderId") String senderId, @Param("recipientId") String recipientId);
}
