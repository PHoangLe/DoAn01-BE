package com.pescue.pescue.repository;

import com.pescue.pescue.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
