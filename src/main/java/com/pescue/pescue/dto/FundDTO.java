package com.pescue.pescue.dto;

import com.pescue.pescue.model.constant.FundType;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FundDTO {
    private String fundName;
    private String fundCover;
    private String fundDescription;
    private BigDecimal valuePerDonationPackage;
    private FundType fundType;
}
