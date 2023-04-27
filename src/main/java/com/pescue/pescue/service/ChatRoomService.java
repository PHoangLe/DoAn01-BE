package com.pescue.pescue.service;

import com.pescue.pescue.model.ChatRoom;
import com.pescue.pescue.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {
    private final ChatRoomRepository repository;

    public Optional<String> getChatId(String senderId, String recipientId, boolean createIfNotExist){
        return repository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if(!createIfNotExist)
                        return Optional.empty();
                    var chatId = String.format("%s_%s", senderId, recipientId);

                    ChatRoom senderRecipient = ChatRoom.builder()
                            .chatId(chatId)
                            .senderId(senderId)
                            .recipientId(recipientId)
                            .build();

                    ChatRoom recipientSender = ChatRoom.builder()
                            .chatId(chatId)
                            .senderId(recipientId)
                            .recipientId(senderId)
                            .build();

                    repository.save(senderRecipient);
                    repository.save(recipientSender);

                    return Optional.of(chatId);
                });
    }
}
