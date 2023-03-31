package com.pescue.pescue.repository;

import com.pescue.pescue.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findUserByUserEmail(String userEmail);
    Optional<User> findUserByUserID(String userID);
}
