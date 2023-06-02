package com.pescue.pescue.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
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
    private String chatRoomID;
    @DBRef
    private User user1;
    @DBRef
    private User user2;

    public ChatRoom(User user1, User user2){
        this.user1 = user1;
        this.user2 = user2;
    }
}