package com.pescue.pescue.model;

import com.pescue.pescue.dto.FundDTO;
import com.pescue.pescue.model.constant.FundType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

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
    private BigDecimal valuePerDonationPackage;
    private FundType fundType;
    private boolean isDelete = false;
    public Fund(FundDTO dto) {
        this.fundName = dto.getFundName();
        this.fundCover = dto.getFundCover();
        this.fundDescription = dto.getFundDescription();
        this.valuePerDonationPackage = dto.getValuePerDonationPackage();
        this.fundBalance = new BigDecimal(0);
        this.fundType = dto.getFundType();
    }

    public Fund(String fundID, FundDTO dto) {
        this.fundID = fundID;
        this.fundName = dto.getFundName();
        this.fundCover = dto.getFundCover();
        this.fundDescription = dto.getFundDescription();
        this.valuePerDonationPackage = dto.getValuePerDonationPackage();
        this.fundBalance = new BigDecimal(0);
        this.fundType = dto.getFundType();
    }
}
