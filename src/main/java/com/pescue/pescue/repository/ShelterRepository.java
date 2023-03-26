package com.pescue.pescue.repository;

import com.pescue.pescue.model.Shelter;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ShelterRepository extends MongoRepository<Shelter, String> {
    Optional<Shelter> findByUserID(String userID);
}
