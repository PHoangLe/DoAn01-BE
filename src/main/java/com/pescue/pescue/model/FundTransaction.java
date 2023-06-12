package com.pescue.pescue.model;

import com.pescue.pescue.model.constant.TransactionType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
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
    @DBRef
    private Fund fund;
    @DBRef
    private User user;
    private Date date;
    private BigDecimal value;

    public FundTransaction(TransactionType type, Fund fund, User user, Date date, BigDecimal value){
        this.transactionType = type;
        this.fund = fund;
        this.user = user;
        this.date = date;
        this.value = value;
    }
}
