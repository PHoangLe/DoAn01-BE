package com.pescue.pescue.repository;

import com.pescue.pescue.model.AdoptionApplication;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdoptionApplicationRepository extends MongoRepository<AdoptionApplication, String> {
    Optional<AdoptionApplication> findByApplicationID(@Param("applicationID") String applicationID);
    Optional<AdoptionApplication> findByUserIDAndAnimalID(@Param("userID") String userID, @Param("animalID") String animalID);
    List<AdoptionApplication> findAllByShelterID(@Param("shelterID") String shelterID);
}
