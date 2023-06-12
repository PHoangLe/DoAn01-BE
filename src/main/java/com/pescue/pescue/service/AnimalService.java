package com.pescue.pescue.service;

import com.pescue.pescue.dto.AnimalDTO;
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
        return animalRepository.findAllByIsAdopted(false);
    }

    public void addAnimal(AnimalDTO dto) throws ExistedException {
        Animal animal = findAnimalByAnimalNameAndShelterID(dto.getAnimalName(), dto.getShelterID());

        if (animal != null)
            throw new ExistedException("Đã tồn tại bé cùng tên trong trại");

        animalRepository.insert(new Animal(dto));
    }

    public void updateAnimal(Animal animal) throws ExistedException {
        Shelter shelter = shelterService.getShelterByShelterID(animal.getShelterID());

        if (shelter == null)
            throw new ShelterNotFoundException();

        Animal tempAnimal = findAnimalByAnimalNameAndShelterID(animal.getAnimalName(), animal.getShelterID());

        if (tempAnimal != null && !Objects.equals(tempAnimal.getAnimalID(), animal.getAnimalID()))
            throw new ExistedException("Đã tồn tại bé cùng tên trong trại");

        animalRepository.save(animal);
        log.trace("Animal information have been saved in the database: " + animal);
    }

    public void deleteAnimal(String animalID){
        deleteRelatedApplication(animalID);
        animalRepository.deleteById(animalID);
        log.trace("Animal information have been deleted in the database: " + animalID);
    }

    private void deleteRelatedApplication(String animalID) {
        Query query = new Query(
                Criteria
                        .where("animalID").is(animalID));
        mongoOperations.remove(query, AdoptionApplication.class);
        mongoOperations.remove(query, OnlineAdoptionApplication.class);
//        mongoOperations.(query, update, AdoptionApplication.class);
        log.trace("Removed applications related to animal: " + animalID);
    }

    public Animal findAnimalByAnimalID(String animalID){
        Optional<Animal> animal = animalRepository.findAnimalByAnimalID(animalID);

        if (animal.isPresent()) {
            log.trace("Found animal with animalID: " + animalID);
            return animal.get();
        }
        log.trace("Can't find animal with animalID: " + animalID);
        return null;
    }

    public Animal findAnimalByAnimalNameAndShelterID(String animalID, String shelterID){
        Optional<Animal> animal = animalRepository.findAnimalByAnimalNameAndShelterID(animalID, shelterID);

        if (animal.isPresent()) {
            log.trace("Found animal with animalName and shelterID: " + animalID + ", " + shelterID);
            return animal.get();
        }
        log.trace("Can't find animal with animalName and shelterID: " + animalID + ", " + shelterID);
        return null;
    }

    public List<Animal> findAnimalsByShelterID(String shelterID){
        List<Animal> animals = animalRepository.findAnimalsByShelterID(shelterID);

        if (!animals.isEmpty()) {
            log.trace("Found animals with shelterID: " + shelterID);
            return animals;
        }
        log.trace("Can't find any animal with shelterID: " + shelterID);
        return null;
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
}
