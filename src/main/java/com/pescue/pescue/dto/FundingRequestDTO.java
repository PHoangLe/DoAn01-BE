package com.pescue.pescue.dto;

import com.pescue.pescue.model.constant.FundingRequestStatus;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FundingRequestDTO {
    private String fundID;
    private String shelterID;
    private String reason;
    private BigDecimal value;
}
