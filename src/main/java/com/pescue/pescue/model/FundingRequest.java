package com.pescue.pescue.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pescue.pescue.model.Fund;
import com.pescue.pescue.model.Shelter;
import com.pescue.pescue.model.User;
import com.pescue.pescue.model.constant.FundingRequestStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document("FundingRequest")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FundingRequest {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String requestID;
    @DBRef
    private Fund fund;
    @DBRef
    private User user;
    @DBRef
    private Shelter shelter;
    private String reason;
    private BigDecimal value;
    private FundingRequestStatus requestStatus;

    public FundingRequest(Fund fund, User user, Shelter shelter, String reason, BigDecimal value){
        this.fund = fund;
        this.user = user;
        this.shelter = shelter;
        this.reason = reason;
        this.value = value;
        this.requestStatus = FundingRequestStatus.PENDING;
    }
}
