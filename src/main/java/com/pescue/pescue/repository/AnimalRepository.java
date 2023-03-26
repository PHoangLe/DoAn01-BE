package com.pescue.pescue.repository;

import com.pescue.pescue.model.Animal;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnimalRepository extends MongoRepository<Animal, String> {
}
