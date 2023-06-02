package com.pescue.pescue.repository;

import com.jayway.jsonpath.JsonPath;
import com.pescue.pescue.model.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    Optional<ChatRoom> findByChatRoomID (@Param("chatRoomID") String chatRoomID);
    Optional<ChatRoom> findByUser1AndUser2(@Param("user1.userID") String user1ID, @Param("user2.userID") String user2ID);

    List<ChatRoom> findAllByUser1OrUser2(@Param("user1.userID") String user1ID, @Param("user2.userID") String user2ID);
}
