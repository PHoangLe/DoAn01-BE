package com.pescue.pescue.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("ChatRoom")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ChatRoom {
    @Id
    private String id;
    private String chatId;
    private String senderId;
    private String recipientId;
}
