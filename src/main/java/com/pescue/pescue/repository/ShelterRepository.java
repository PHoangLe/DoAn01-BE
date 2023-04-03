package com.pescue.pescue.repository;

import com.pescue.pescue.model.Shelter;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ShelterRepository extends MongoRepository<Shelter, String> {
    Optional<Shelter> findShelterByUserID(String userID);
    Optional<Shelter> findShelterByShelterID(String shelterID);
    Optional<Shelter> findShelterByShelterName(String shelterName);
}
