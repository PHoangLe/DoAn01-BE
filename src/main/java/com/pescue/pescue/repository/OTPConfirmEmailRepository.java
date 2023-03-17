package com.pescue.pescue.repository;

import com.pescue.pescue.model.OTPConfirmEmail;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OTPConfirmEmailRepository extends MongoRepository<OTPConfirmEmail, String> {
}
