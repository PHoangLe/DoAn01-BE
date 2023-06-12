package com.pescue.pescue.model;

import com.pescue.pescue.model.constant.MessageStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("ChatMessage")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ChatMessage {
    @Id
    private String messageID;
    private String chatRoomID;
    private String senderID;
    private String recipientID;
    private String content;
    private Date timestamp;
    private MessageStatus status;
}
