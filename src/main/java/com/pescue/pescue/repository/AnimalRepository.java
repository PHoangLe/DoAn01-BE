package com.pescue.pescue.repository;

import com.pescue.pescue.model.Animal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface AnimalRepository extends MongoRepository<Animal, String> {
    Optional<Animal> findAnimalByAnimalID(
            @Param("animalID")String animalID);
    Optional<Animal> findAnimalByAnimalIDAndDeletedIsFalse(
            @Param("animalID") String animalID);
    Optional<Animal> findAnimalByAnimalNameAndShelterID(
            @Param("animalName") String animalName,
            @Param("shelterID") String shelterID);
    Optional<Animal> findAnimalByAnimalNameAndShelterIDAndDeletedIsFalse(
            @Param("animalName") String animalName,
            @Param("shelterID") String shelterID);

    List<Animal> findAnimalsByShelterID(
            @Param("shelterID") String shelterID);
    List<Animal> findAnimalsByShelterIDAndDeletedIsFalse(
            @Param("shelterID") String shelterID);
}
