package com.pescue.pescue.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("OTPConfirmEmail")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OTPConfirmEmail {

    @Id
    private String Id;
    private String receiverEmail;
    private Date date;
    private Date expiredDate;
    private String OTP;

    public OTPConfirmEmail(String receiverEmail, Date date, Date expiredDate, String OTP) {
        this.receiverEmail = receiverEmail;
        this.date = date;
        this.expiredDate = expiredDate;
        this.OTP = OTP;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
