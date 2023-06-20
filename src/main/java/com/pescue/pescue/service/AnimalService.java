package com.pescue.pescue.service;

import com.pescue.pescue.dto.AnimalDTO;
import com.pescue.pescue.exception.AnimalNotFoundException;
import com.pescue.pescue.exception.ExistedException;
import com.pescue.pescue.exception.ShelterNotFoundException;
import com.pescue.pescue.model.*;
import com.pescue.pescue.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AnimalService {
    private final AnimalRepository animalRepository;
    private final ShelterService shelterService;
    private final MongoOperations mongoOperations;
    public List<Animal> findAllAnimals(){
        return animalRepository.findAllByIsAdoptedAndIsDeleted(false, false);
    }

    public void addAnimal(AnimalDTO dto) throws ExistedException {
        animalRepository.insert(new Animal(dto));
    }

    public void updateAnimal(Animal animal) throws ExistedException {
        Shelter shelter = shelterService.getShelterByShelterID(animal.getShelterID());

        if (shelter == null)
            throw new ShelterNotFoundException();

        animalRepository.save(animal);
        log.trace("Animal information have been saved in the database: " + animal);
    }

    public void deleteAnimal(String animalID){
        Animal animal = findAnimalByAnimalID(animalID);

        if (animal == null)
            throw new AnimalNotFoundException();

        animal.setDeleted(true);
        updateAnimal(animal);
    }

    private void deleteRelatedApplication(String animalID) {
        Query query = new Query(
                Criteria
                        .where("animalID").is(animalID));
        mongoOperations.remove(query, AdoptionApplication.class);
        mongoOperations.remove(query, OnlineAdoptionApplication.class);
        log.trace("Removed applications related to animal: " + animalID);
    }

    public Animal findAnimalByAnimalID(String animalID){
        return animalRepository.findAnimalByAnimalID(animalID).orElse(null);
    }

    public Animal findAnimalByAnimalNameAndShelterID(String animalID, String shelterID){
        return animalRepository.findAnimalByAnimalNameAndShelterID(animalID, shelterID).orElse(null);
    }

    public List<Animal> findAnimalsByShelterID(String shelterID){
        return animalRepository.findAnimalsByShelterID(shelterID);
    }

    public void addOnlineAdopters(Animal animal, User adopter) throws ExistedException {
        List<User> onlineAdopters = animal.getOnlineAdopters();
        if (onlineAdopters == null)
            onlineAdopters = new ArrayList<>();

        if (!onlineAdopters.contains(adopter)) {
            onlineAdopters.add(adopter);
            animal.setOnlineAdopters(onlineAdopters);

            updateAnimal(animal);
            log.trace("Add adopters for animal: " + animal.getAnimalID() + " User: " + adopter.getUserID());
        }
    }
    public void removeOnlineAdopters(Animal animal, User user) throws ExistedException {
        List<User> onlineAdopters = animal.getOnlineAdopters();

        List<User> collect = onlineAdopters.stream()
                .filter(user1 -> !user1.getUserID().equals(user.getUserID()))
                .toList();

        animal.setOnlineAdopters(collect);
        updateAnimal(animal);
        log.trace("Updated online adopters for Animal: " + animal.getAnimalID());
    }

    public long[] countAdoptedAndNotAdopted() {
        List<Animal> animals = findAllAnimals();

        long adoptedAnimal = animals.stream()
                .filter(Animal::isAdopted).count();

        long notAdoptedAnimal = animals.stream()
                .filter((animal) -> !animal.isAdopted()).count();

        return new long[]{adoptedAnimal, notAdoptedAnimal};
    }
}
