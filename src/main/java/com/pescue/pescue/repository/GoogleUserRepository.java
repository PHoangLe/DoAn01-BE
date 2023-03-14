package com.pescue.pescue.repository;

import com.pescue.pescue.model.GoogleUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GoogleUserRepository extends MongoRepository<GoogleUser, String> {

    GoogleUser findUserByUserEmail(String userEmail);
}
