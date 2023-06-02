package com.pescue.pescue.service;

import com.pescue.pescue.model.ChatRoom;
import com.pescue.pescue.model.User;
import com.pescue.pescue.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {
    private final ChatRoomRepository repository;
    private final UserService userService;
    public Optional<String> getChatId(String senderId, String recipientId, boolean createIfNotExist){
        User sender = userService.findUserByID(senderId);
        User recipient = userService.findUserByID(recipientId);

        return repository
                .findByUser1OrUser2AndUser2OrUser1(senderId, recipientId)
                .map(ChatRoom::getChatRoomID)
                .or(() -> {
                    if(!createIfNotExist)
                        return Optional.empty();

                    ChatRoom newChatRoom = new ChatRoom(sender, recipient);

                    repository.insert(newChatRoom);
                    String chatRoomID = newChatRoom.getChatRoomID();

                    return Optional.of(chatRoomID);
                });
    }

    public List<ChatRoom> findAllChatRoomByUserID(String userID) {
        return repository.findAllByUser1OrUser2(userID, userID);
    }
}
