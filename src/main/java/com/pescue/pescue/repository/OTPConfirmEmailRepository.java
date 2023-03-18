package com.pescue.pescue.repository;

import com.pescue.pescue.model.OTPConfirmEmail;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OTPConfirmEmailRepository extends MongoRepository<OTPConfirmEmail, String> {

    List<OTPConfirmEmail> findOTPConfirmEmailsByReceiverEmailOrderByDate (String receiverEmail);
}
