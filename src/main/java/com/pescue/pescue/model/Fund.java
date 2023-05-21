package com.pescue.pescue.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Currency;

@Document("Fund")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Fund {
    @Id
    private String fundID;
    private String fundCover;
    private String fundName;
    private String fundDescription;
    private BigDecimal fundBalance;

    public Fund(String fundName, String fundCover, String fundDescription) {
        this.fundName = fundName;
        this.fundCover = fundCover;
        this.fundDescription = fundDescription;
        this.fundBalance = new BigDecimal(0);
    }
}
