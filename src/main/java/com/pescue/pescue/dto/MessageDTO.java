package com.pescue.pescue.dto;

import lombok.*;

import java.util.Date;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private String senderID;
    private String recipientID;
    private String content;
}
