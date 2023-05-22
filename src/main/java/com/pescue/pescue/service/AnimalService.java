package com.pescue.pescue.service;

import com.pescue.pescue.dto.AnimalDTO;
import com.pescue.pescue.dto.UserDTO;
import com.pescue.pescue.model.Animal;
import com.pescue.pescue.model.User;
import com.pescue.pescue.repository.AnimalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AnimalService {
    @Autowired
    AnimalRepository animalRepository;

    Logger logger = LoggerFactory.getLogger(AnimalService.class);

    public List<Animal> findAllAnimals(){
        return animalRepository.findAllByIsAdopted(false);
    }

    public boolean addAnimal(AnimalDTO animal){
        try {
            animalRepository.insert(new Animal(animal));
            return true;
        }
        catch (Exception e){
            logger.trace("There is an error occur while inserting animal to database: " + animal);
            return false;
        }
    }

    public boolean updateAnimal(Animal animal){
        try {
            animalRepository.save(animal);
        }
        catch (Exception e){
            logger.error("There is an error occur while saving animal information: " + animal);
            return false;
        }
        logger.trace("Animal information have been saved in the database: " + animal);
        return true;
    }

    public boolean deleteAnimal(Animal animal){
        try {
            animalRepository.delete(animal);
        }
        catch (Exception e){
            logger.error("There is an error occur while deleting animal information: " + animal);
            return false;
        }
        logger.trace("Animal information have been deleted in the database: " + animal);
        return true;
    }

    public Animal findAnimalByAnimalID(String animalID){
        Optional<Animal> animal = animalRepository.findAnimalByAnimalID(animalID);

        if (animal.isPresent()) {
            logger.trace("Found animal with animalID: " + animalID);
            return animal.get();
        }
        logger.trace("Can't find animal with animalID: " + animalID);
        return null;
    }

    public Animal findAnimalByAnimalNameAndShelterID(String animalID, String shelterID){
        Optional<Animal> animal = animalRepository.findAnimalByAnimalNameAndShelterID(animalID, shelterID);

        if (animal.isPresent()) {
            logger.trace("Found animal with animalName and shelterID: " + animalID + ", " + shelterID);
            return animal.get();
        }
        logger.trace("Can't find animal with animalName and shelterID: " + animalID + ", " + shelterID);
        return null;
    }

    public List<Animal> findAnimalsByShelterID(String shelterID){
        List<Animal> animals = animalRepository.findAnimalsByShelterID(shelterID);

        if (!animals.isEmpty()) {
            logger.trace("Found animals with shelterID: " + shelterID);
            return animals;
        }
        logger.trace("Can't find any animal with shelterID: " + shelterID);
        return null;
    }

    public void addOnlineAdopters(Animal animal, User adopter) {
        List<User> onlineAdopters = animal.getOnlineAdopters();
        if (onlineAdopters == null)
            onlineAdopters = new ArrayList<>();

        if (!onlineAdopters.contains(adopter)) {
            onlineAdopters.add(adopter);
            animal.setOnlineAdopters(onlineAdopters);

            updateAnimal(animal);
            logger.trace("Add adopters for animal: " + animal.getAnimalID() + " User: " + adopter.getUserID());
        }
    }
}
