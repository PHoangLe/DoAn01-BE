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
    Optional<Animal> findAnimalByAnimalIDAndAdopted(
            @Param("animalID")String animalID,
            @Param("isAdopted") boolean isAdopted);

    Optional<Animal> findAnimalByAnimalNameAndShelterID(
            @Param("animalName") String animalName,
            @Param("shelterID") String shelterID);
    Optional<Animal> findAnimalByAnimalNameAndShelterIDAndAdopted(
            @Param("animalName") String animalName,
            @Param("shelterID") String shelterID,
            @Param("isAdopted") boolean isAdopted);

    List<Animal> findAnimalsByShelterID(
            @Param("shelterID") String shelterID);
    List<Animal> findAnimalsByShelterIDAndAdopted(
            @Param("shelterID") String shelterID,
            @Param("isAdopted") boolean isAdopted);

    List<Animal> findAllByIsAdoptedAndIsDeleted(
            @Param("isAdopted") boolean isAdopted,
            @Param("isDeleted") boolean isDeleted
    );
}
