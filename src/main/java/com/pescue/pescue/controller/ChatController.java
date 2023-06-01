package com.pescue.pescue.controller;

import com.pescue.pescue.dto.MessageDTO;
import com.pescue.pescue.model.ChatMessage;
import com.pescue.pescue.model.ChatNotification;
import com.pescue.pescue.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/private-message")
    public ChatMessage processMessage(@Payload MessageDTO messageDTO){
        log.trace(messageDTO.getContent());
        ChatMessage chatMessage = chatMessageService.save(messageDTO);
        if (chatMessage == null)
            return null;
        messagingTemplate.convertAndSendToUser(chatMessage.getRecipient().getUserID(),"/private",chatMessage); // /user/userID/private
        return chatMessage;
    }

//    @MessageMapping("/message")
//    @SendTo("/chatroom/public")
//    public ChatMessage receiveMessage(@Payload ChatMessage message){
//        return message;
//    }

//    @GetMapping("/messages/{senderId}/{recipientId}/count")
//    public ResponseEntity<Long> countNewMessages(@PathVariable String recipientId, @PathVariable String senderId){
//        return ResponseEntity.ok(chatMessageService.countNewMessage(senderId, recipientId));
//    }

//    @GetMapping("/messages/{senderId}/{recipientId}")
//    public ResponseEntity<?> findChatMessages ( @PathVariable String senderId,
//                                                @PathVariable String recipientId) {
//        return ResponseEntity
//                .ok(chatMessageService.findChatMessages(senderId, recipientId));
//    }

//    @GetMapping("/messages/{id}")
//    public ResponseEntity<?> findMessage(@PathVariable String id){
//        return ResponseEntity.ok(chatMessageService.findById(id));
//    }
}
