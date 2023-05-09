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
    private String fundName;
    private BigDecimal fundBalance;
}
