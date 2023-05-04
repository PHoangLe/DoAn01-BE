package com.pescue.pescue.repository;

import com.pescue.pescue.model.AdoptionApplication;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdoptionApplicationRepository extends MongoRepository<AdoptionApplication, String> {
    Optional<AdoptionApplication> findByApplicationID(@Param("applicationID") String applicationID);
}
