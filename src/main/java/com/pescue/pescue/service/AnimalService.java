package com.pescue.pescue.service;

import com.pescue.pescue.dto.AnimalDTO;
import com.pescue.pescue.exception.AnimalNotFoundException;
import com.pescue.pescue.exception.ExistedException;
import com.pescue.pescue.exception.ShelterNotFoundException;
import com.pescue.pescue.model.*;
import com.pescue.pescue.model.constant.ApplicationStatus;
import com.pescue.pescue.repository.AnimalRepository;
import com.pescue.pescue.repository.OnlineAdoptionApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AnimalService {
    private final AnimalRepository animalRepository;
    private final ShelterService shelterService;
    private final MongoOperations mongoOperations;
    private final OnlineAdoptionApplicationRepository onlineAdoptionApplicationRepository;
    public List<Animal> getAllAnimals(){
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
        log.trace("New Animal: " + animal);
    }

    public void deleteAnimal(String animalID){
        Animal animal = getAnimalByAnimalID(animalID);

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

    public Animal getAnimalByAnimalID(String animalID){
        return animalRepository.findAnimalByAnimalID(animalID).orElse(null);
    }

    public List<User> getAnimalOnlineAdopters(String animalID){
        List<OnlineAdoptionApplication> allByAnimal = onlineAdoptionApplicationRepository.findAllByAnimal(animalID);

        return allByAnimal.stream()
                .filter((application) -> application.getApplicationStatus() == ApplicationStatus.COMPLETED)
                .map(OnlineAdoptionApplication::getUser)
                .distinct()
                .toList();
    }

    public Animal findAnimalByAnimalNameAndShelterID(String animalID, String shelterID){
        return animalRepository.findAnimalByAnimalNameAndShelterID(animalID, shelterID).orElse(null);
    }

    public List<Animal> getAnimalsByShelterID(String shelterID){
        return animalRepository.findAnimalsByShelterID(shelterID);
    }
    public long[] countAdoptedAndNotAdopted() {
        List<Animal> animals = animalRepository.findAll();

        long adoptedAnimal = animals.stream()
                .filter(Animal::isAdopted).count();

        long notAdoptedAnimal = animals.size() - adoptedAnimal;

        return new long[]{adoptedAnimal, notAdoptedAnimal};
    }
}
