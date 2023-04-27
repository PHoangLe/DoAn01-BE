package com.pescue.pescue.controller;

import com.pescue.pescue.model.ChatMessage;
import com.pescue.pescue.model.ChatNotification;
import com.pescue.pescue.service.ChatMessageService;
import com.pescue.pescue.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@CrossOrigin
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage){
        var chatId = chatRoomService
                .getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
        chatMessage.setChatId(chatId.get());

        ChatMessage saved = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                new ChatNotification(
                        saved.getId(),
                        saved.getSenderId(),
                        saved.getSenderName()
                )
        );
    }

    @GetMapping("/messages/{senderId}/{recipientId}/count")
    public ResponseEntity<Long> countNewMessages(@PathVariable String recipientId, @PathVariable String senderId){
        return ResponseEntity.ok(chatMessageService.countNewMessage(senderId, recipientId));
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<?> findChatMessages ( @PathVariable String senderId,
                                                @PathVariable String recipientId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<?> findMessage(@PathVariable String id){
        return ResponseEntity.ok(chatMessageService.findById(id));
    }
}
