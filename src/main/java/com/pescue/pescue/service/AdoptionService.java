package com.pescue.pescue.service;

import com.pescue.pescue.dto.AdoptionApplicationDTO;
import com.pescue.pescue.model.AdoptionApplication;
import com.pescue.pescue.model.Animal;
import com.pescue.pescue.repository.AdoptionApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdoptionService {
    private final AdoptionApplicationRepository repository;
    private final AnimalService animalService;
    public boolean createAdoptionRequest(AdoptionApplicationDTO dto){
        AdoptionApplication application = new AdoptionApplication(dto);

        try{
            repository.insert(application);
            log.trace("added adoption application for user: " + application.getUserID() + " pet: " + application.getAnimalID());
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    public AdoptionApplication findByApplicationID(String applicationID){
        return repository.findByApplicationID(applicationID).orElse(null);

    }

    public boolean confirmAdoptionRequest(String applicationID) {
        AdoptionApplication application = findByApplicationID(applicationID);

        if (application == null)
            return false;

        Animal animal = animalService.findAnimalByAnimalID(application.getAnimalID());

        application.setApplicationStatus("completed");
        animal.setAdopted(true);

        try {
            repository.save(application);
            animalService.updateAnimal(animal);

            log.trace("Approved application with ID: " + applicationID);
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }

        return true;
    }

    public boolean declineAdoptionRequest(String applicationID) {
        AdoptionApplication application = findByApplicationID(applicationID);

        if (application == null)
            return false;

        Animal animal = animalService.findAnimalByAnimalID(application.getAnimalID());

        application.setApplicationStatus("declined");

        try {
            repository.save(application);

            log.trace("Declined application with ID: " + applicationID);
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }

        return true;
    }
}
