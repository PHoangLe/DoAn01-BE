package com.pescue.pescue.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ChatNotification {
    private String id;
    private String senderId;
    private String senderName;
}
