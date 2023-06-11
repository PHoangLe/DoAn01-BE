package com.pescue.pescue.model;

import com.pescue.pescue.dto.DonationDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.concurrent.atomic.DoubleAccumulator;

@Document("Donation")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Donation {
    @Id
    private String donationID;
    @DBRef
    private User user;
    @DBRef
    private Fund fund;
    private Integer numsOfPackage;
    private DonationStatus donationStatus;

    public Donation(User user, Fund fund, Integer numsOfPackage) {
        this.user = user;
        this.fund = fund;
        this.numsOfPackage = numsOfPackage;
        this.donationStatus = DonationStatus.PENDING;
    }
}
