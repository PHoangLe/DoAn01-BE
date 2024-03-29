package com.pescue.pescue.model;

import com.pescue.pescue.model.constant.DonationStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

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
    private Date date;
    public Donation(User user, Fund fund, Integer numsOfPackage) {
        this.user = user;
        this.fund = fund;
        this.numsOfPackage = numsOfPackage;
        this.donationStatus = DonationStatus.PENDING;
        this.date = new Date(System.currentTimeMillis());
    }
}
