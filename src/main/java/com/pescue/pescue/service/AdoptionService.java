package com.pescue.pescue.service;

import com.pescue.pescue.dto.AdoptionApplicationDTO;
import com.pescue.pescue.dto.AdoptionApplicationRequestDTO;
import com.pescue.pescue.exception.AnimalNotFoundException;
import com.pescue.pescue.exception.ApplicationNotFoundException;
import com.pescue.pescue.exception.ShelterNotFoundException;
import com.pescue.pescue.exception.UserNotFoundException;
import com.pescue.pescue.model.*;
import com.pescue.pescue.repository.AdoptionApplicationRepository;
import com.pescue.pescue.repository.OnlineAdoptionApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdoptionService {
    private final AdoptionApplicationRepository adoptionApplicationRepository;
    private final OnlineAdoptionApplicationRepository onlineAdoptionApplicationRepository;
    private final AnimalService animalService;
    private final UserService userService;
    private final ShelterService shelterService;


    //Offline Adoption
    public void createAdoptionRequest(AdoptionApplicationRequestDTO dto){
        AdoptionApplication application = new AdoptionApplication(dto);

        User user = userService.findUserByID(dto.getUserID());
        Animal animal = animalService.findAnimalByAnimalID(dto.getAnimalID());
        Shelter shelter = shelterService.findShelterByShelterID(dto.getShelterID());

        if (user == null) {
            log.trace("User not found ID: " + dto.getUserID());
            throw new UserNotFoundException();
        }
        if (animal == null) {
            log.trace("Animal not found ID: " + dto.getAnimalID());
            throw new AnimalNotFoundException();
        }
        if (shelter == null) {
            log.trace("Shelter not found ID: " + dto.getShelterID());
            throw new ShelterNotFoundException();
        }
        adoptionApplicationRepository.insert(application);
        log.trace("Added adoption application for user: " + application.getUserID() + " pet: " + application.getAnimalID());
    }
    public AdoptionApplication findApplicationByApplicationID(String applicationID){
        log.trace("Finding adoption application with ID: " + applicationID);
        return adoptionApplicationRepository.findByApplicationID(applicationID).orElse(null);
    }

    public void confirmAdoptionRequest(String applicationID) {
        AdoptionApplication application = findApplicationByApplicationID(applicationID);

        if (application == null){
            log.trace("Can't find application with ID: " + applicationID);
            throw new ApplicationNotFoundException();
        }

        application.setApplicationStatus(ApplicationStatus.COMPLETED);
        adoptionApplicationRepository.save(application);
        Animal animal = animalService.findAnimalByAnimalID(application.getAnimalID());
        animal.setAdopted(true);
        animalService.updateAnimal(animal);

        log.trace("Approved application with ID: " + applicationID);
    }
    public void declineAdoptionRequest(String applicationID) {
        AdoptionApplication application = findApplicationByApplicationID(applicationID);

        if (application == null){
            log.trace("Can't find application with ID: " + applicationID);
            throw new ApplicationNotFoundException();
        }

        application.setApplicationStatus(ApplicationStatus.REJECTED);
        adoptionApplicationRepository.save(application);

        log.trace("Declined application with ID: " + applicationID);
    }
    public List<AdoptionApplicationDTO> findApplicationByShelterID(String shelterID) {
        Shelter shelterByShelterID = shelterService.findShelterByShelterID(shelterID);

        if (shelterByShelterID == null){
            log.trace("Shelter not found ID: " + shelterID);
            throw new ShelterNotFoundException();
        }

        List<AdoptionApplication> adoptionApplicationList = adoptionApplicationRepository.findAllByShelterID(shelterID);

        List<AdoptionApplicationDTO> applicationDTOS = new ArrayList<>();
        adoptionApplicationList.forEach(application -> {
            User user = userService.findUserByID(application.getUserID());
            Animal animal = animalService.findAnimalByAnimalID(application.getAnimalID());
            Shelter shelter = shelterService.findShelterByShelterID(application.getShelterID());

            applicationDTOS.add(new AdoptionApplicationDTO(application, user, animal, shelter));
        });

        return applicationDTOS;
    }


    //Online Adoption
    public void createOnlineAdoptionRequest(AdoptionApplicationRequestDTO dto){
        OnlineAdoptionApplication application = new OnlineAdoptionApplication(dto);

        User user = userService.findUserByID(dto.getUserID());
        Animal animal = animalService.findAnimalByAnimalID(dto.getAnimalID());
        Shelter shelter = shelterService.findShelterByShelterID(dto.getShelterID());

        if (user == null) {
            log.trace("User not found ID: " + dto.getUserID());
            throw new UserNotFoundException();
        }
        if (animal == null) {
            log.trace("Animal not found ID: " + dto.getAnimalID());
            throw new AnimalNotFoundException();
        }
        if (shelter == null) {
            log.trace("Shelter not found ID: " + dto.getShelterID());
            throw new ShelterNotFoundException();
        }
        onlineAdoptionApplicationRepository.insert(application);
        log.trace("added online adoption application for user: " + application.getUserID() + " pet: " + application.getAnimalID());

    }
    public OnlineAdoptionApplication findOnlineApplicationByApplicationID(String applicationID){
        log.trace("Finding adoption application with ID: " + applicationID);
        return onlineAdoptionApplicationRepository.findByApplicationID(applicationID).orElse(null);
    }
    public void confirmOnlineAdoptionRequest(String applicationID) {
        OnlineAdoptionApplication application = findOnlineApplicationByApplicationID(applicationID);

        if (application == null) {
            log.trace("Can't find application with ID: " + applicationID);
            throw new ApplicationNotFoundException();
        }

        Animal animal = animalService.findAnimalByAnimalID(application.getAnimalID());
        User user = userService.findUserByID(application.getUserID());

        application.setApplicationStatus(ApplicationStatus.COMPLETED);
        onlineAdoptionApplicationRepository.save(application);
        animalService.addOnlineAdopters(animal, user);

        log.trace("Approved online application with ID: " + applicationID);

    }
    public void declineOnlineAdoptionRequest(String applicationID) {
        OnlineAdoptionApplication application = findOnlineApplicationByApplicationID(applicationID);

        if (application == null) {
            log.trace("Can't find application with ID: " + applicationID);
            throw new ApplicationNotFoundException();
        }

        application.setApplicationStatus(ApplicationStatus.REJECTED);
        onlineAdoptionApplicationRepository.save(application);

        log.trace("Declined online application with ID: " + applicationID);
    }
    public List<OnlineAdoptionApplication> getAllOnlineApplication(){
        return onlineAdoptionApplicationRepository.findAll();
    }
}
