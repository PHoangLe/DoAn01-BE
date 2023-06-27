package com.pescue.pescue.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("OTPForgotPassword")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OTPForgotPassword extends OTPConfirmEmail{
    public OTPForgotPassword(String emailAddress, Date date, Date expiredDate, String otp) {
        super(emailAddress, date, expiredDate, otp);
    }
}
