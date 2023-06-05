package com.pescue.pescue.service;

import com.pescue.pescue.model.ChatMessage;
import com.pescue.pescue.model.ChatRoom;
import com.pescue.pescue.model.MessageStatus;
import com.pescue.pescue.model.User;
import com.pescue.pescue.repository.ChatMessageRepository;
import com.pescue.pescue.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;
    private final MongoOperations mongoOperations;
    public Optional<String> getChatId(String senderId, String recipientId, boolean createIfNotExist){
        User sender = userService.findUserByID(senderId);
        User recipient = userService.findUserByID(recipientId);

        return chatRoomRepository
                .findByUser1AndUser2(senderId, recipientId)
                .map(ChatRoom::getChatRoomID)
                .or(() -> {
                    if(!createIfNotExist)
                        return Optional.empty();

                    return chatRoomRepository.findByUser1AndUser2(recipientId, senderId)
                            .map(ChatRoom::getChatRoomID)
                            .or(() -> {
                                ChatRoom newChatRoom = new ChatRoom(sender, recipient);

                                chatRoomRepository.insert(newChatRoom);
                                String chatRoomID = newChatRoom.getChatRoomID();

                                return Optional.of(chatRoomID);
                            });
                });
    }

    public List<ChatRoom> findAllChatRoomByUserID(String userID) {
        return chatRoomRepository.findAllByUser1OrUser2(userID, userID);
    }

    public List<ChatMessage> findAllChatMessageByChatRoomID(String chatRoomID) {
        return chatMessageRepository.findAllByChatRoomID(chatRoomID);
    }

    public void updateStatuses(String senderId, String recipientId, MessageStatus status) {
        Query query = new Query(
                Criteria
                        .where("senderId").is(senderId)
                        .and("recipientId").is(recipientId));
        Update update = Update.update("status", status);
        mongoOperations.updateMulti(query, update, ChatMessage.class);
    }

    public List<ChatMessage> findAllMessageBySenderIDAndRecipientID(String senderID, String recipientID) {
        updateStatuses(senderID, recipientID, MessageStatus.RECEIVED);
        return chatMessageRepository.findAllBySenderIDAndRecipientID(senderID, recipientID);
    }
}
