package com.pescue.pescue.repository;

import com.pescue.pescue.model.OnlineAdoptionApplication;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OnlineAdoptionApplicationRepository extends MongoRepository<OnlineAdoptionApplication, String> {
    Optional<OnlineAdoptionApplication> findByApplicationID(@Param("applicationID") String applicationID);
    Optional<OnlineAdoptionApplication> findByUserAndAnimal(@Param("userID") String userID, @Param("animalID") String animalID);
}
