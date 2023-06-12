package com.pescue.pescue.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("FundingRequest")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FundingRequest {
    @Id
    private String requestID;
    @DBRef
    private Fund fund;
    @DBRef
    private User user;
    @DBRef
    private Shelter shelter;
}
