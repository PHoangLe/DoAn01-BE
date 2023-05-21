package com.pescue.pescue.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Document("FundTransaction")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FundTransaction {
    @Id
    private String transactionID;
    private TransactionType transactionType;
    private String source;
    private String destination;
    private Date date;
    private BigDecimal value;
}
