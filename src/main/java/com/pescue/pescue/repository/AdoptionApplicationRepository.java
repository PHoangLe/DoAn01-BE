package com.pescue.pescue.repository;

import com.pescue.pescue.dto.UserDTO;
import com.pescue.pescue.model.AdoptionApplication;
import com.pescue.pescue.model.Animal;
import com.pescue.pescue.model.Shelter;
import com.pescue.pescue.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdoptionApplicationRepository extends MongoRepository<AdoptionApplication, String> {
    Optional<AdoptionApplication> findByApplicationID(@Param("applicationID") String applicationID);
    Optional<AdoptionApplication> findByUserAndAnimal(@Param("user") UserDTO userDTO, @Param("animal") Animal animal);
    List<AdoptionApplication> findAllByShelter(@Param("shelter") Shelter shelter);
}
