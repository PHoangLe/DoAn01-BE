package com.pescue.pescue.repository;

import com.pescue.pescue.model.AdoptionApplication;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdoptionApplicationRepository extends MongoRepository<AdoptionApplication, String> {
}
