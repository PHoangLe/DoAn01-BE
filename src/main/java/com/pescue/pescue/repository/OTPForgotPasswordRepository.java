package com.pescue.pescue.repository;

import com.pescue.pescue.model.OTPConfirmEmail;
import com.pescue.pescue.model.OTPForgotPassword;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OTPForgotPasswordRepository extends MongoRepository<OTPForgotPassword, String> {
    List<OTPForgotPassword> findOTPForgotPasswordsByReceiverEmailOrderByDate (String receiverEmail);

}
