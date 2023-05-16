package com.pescue.pescue.service;

import com.pescue.pescue.dto.AdoptionApplicationRequestDTO;
import com.pescue.pescue.exception.*;
import com.pescue.pescue.model.*;
import com.pescue.pescue.repository.AdoptionApplicationRepository;
import com.pescue.pescue.repository.OnlineAdoptionApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private final EmailService emailService;

    private void sendResultEmail(String receiverEmail, boolean isConfirm) throws SendMailFailedException {
        String emailBody;
        String subject = "Nhận nuôi";

        if (isConfirm){
            emailBody = """
                    Chúc mừng bạn,
                    Yêu cầu nhận nuôi của bạn đã được xử lý xong.
                    Chúng tôi đại diện trại cứu trợ và bé được bạn nhận nuôi gửi đến bạn một lời cảm ơn chân thành.
                    Nếu có bất cứ thắc mắc nào hãy liên hệ lại với chúng tôi qua email này.
                    Pescue.""";
        }else {
            emailBody = """
                    Chào bạn,
                    Yêu cầu nhận nuôi của bạn đã được xử lý xong.
                    Nhưng chúng tôi lấy làm tiếc về việc nhận nuôi của bạn đã không thành công.
                    Nếu có bất cứ thắc mắc nào hãy liên hệ lại với chúng tôi qua email này.
                    Pescue.""";
        }
        emailService.sendMail(receiverEmail, emailBody, subject);
    }
    //Offline Adoption
    public void createAdoptionRequest(AdoptionApplicationRequestDTO dto) throws ApplicationExistedException {
        AdoptionApplication existedApplication = findApplicationByUserIDAndAnimalID(dto.getUserID(), dto.getAnimalID());
        if(existedApplication != null && existedApplication.getApplicationStatus() != ApplicationStatus.REJECTED){
            log.trace("Application already existed: User " + existedApplication.getUser().getUserID() + " Animal " + existedApplication.getAnimal().getAnimalID());
            throw new ApplicationExistedException();
        }

        User user = userService.findUserByID(dto.getUserID());
        if (user == null) {
            log.trace("User not found ID: " + dto.getUserID());
            throw new UserNotFoundException();
        }

        Animal animal = animalService.findAnimalByAnimalID(dto.getAnimalID());
        if (animal == null) {
            log.trace("Animal not found ID: " + dto.getAnimalID());
            throw new AnimalNotFoundException();
        }

        Shelter shelter = shelterService.findShelterByShelterID(dto.getShelterID());
        if (shelter == null) {
            log.trace("Shelter not found ID: " + dto.getShelterID());
            throw new ShelterNotFoundException();
        }

        AdoptionApplication application = new AdoptionApplication(animal, shelter, user);

        adoptionApplicationRepository.insert(application);
        log.trace("Added adoption application for user: " + application.getUser().getUserID() + " pet: " + application.getAnimal().getAnimalID());
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
        Animal animal = animalService.findAnimalByAnimalID(application.getAnimal().getAnimalID());
        animal.setAdopted(true);
        animalService.updateAnimal(animal);

        User user = userService.findUserByID(application.getUser().getUserID());
        sendResultEmail(user.getUserEmail(), true);

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

        User user = userService.findUserByID(application.getUser().getUserID());
        sendResultEmail(user.getUserEmail(), false);

        log.trace("Declined application with ID: " + applicationID);
    }
    public List<AdoptionApplication> findApplicationByShelterID(String shelterID) {
        Shelter shelterByShelterID = shelterService.findShelterByShelterID(shelterID);

        if (shelterByShelterID == null){
            log.trace("Shelter not found ID: " + shelterID);
            throw new ShelterNotFoundException();
        }

        return adoptionApplicationRepository.findAllByShelter(shelterID);
    }
    public AdoptionApplication findApplicationByUserIDAndAnimalID(String userID, String animalID) {
        return adoptionApplicationRepository.findByUserAndAnimal(userID, animalID).orElse(null);
    }


    //Online Adoption
    public void createOnlineAdoptionRequest(AdoptionApplicationRequestDTO dto) throws ApplicationExistedException {
        OnlineAdoptionApplication existedApplication = findOnlineApplicationByUserIDAndAnimalID(dto.getUserID(), dto.getAnimalID());
        if(existedApplication != null){
            if (existedApplication.getApplicationStatus() == ApplicationStatus.PENDING) {
                log.trace("Application already existed: User " + existedApplication.getUser().getUserID() + " Animal " + existedApplication.getAnimal().getAnimalID());
                throw new ApplicationExistedException();
            }
        }
        User user = userService.findUserByID(dto.getUserID());
        if (user == null) {
            log.trace("User not found ID: " + dto.getUserID());
            throw new UserNotFoundException();
        }
        Animal animal = animalService.findAnimalByAnimalID(dto.getAnimalID());
        if (animal == null) {
            log.trace("Animal not found ID: " + dto.getAnimalID());
            throw new AnimalNotFoundException();
        }
        Shelter shelter = shelterService.findShelterByShelterID(dto.getShelterID());
        if (shelter == null) {
            log.trace("Shelter not found ID: " + dto.getShelterID());
            throw new ShelterNotFoundException();
        }

        OnlineAdoptionApplication application = new OnlineAdoptionApplication(animal, shelter, user);

        onlineAdoptionApplicationRepository.insert(application);
        log.trace("added online adoption application for user: " + application.getUser().getUserID() + " pet: " + application.getAnimal().getAnimalID());

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

        Animal animal = animalService.findAnimalByAnimalID(application.getAnimal().getAnimalID());
        User user = userService.findUserByID(application.getUser().getUserID());

        application.setApplicationStatus(ApplicationStatus.COMPLETED);
        onlineAdoptionApplicationRepository.save(application);
        animalService.addOnlineAdopters(animal, user);

        sendResultEmail(user.getUserEmail(), true);

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

        User user = userService.findUserByID(application.getUser().getUserID());
        sendResultEmail(user.getUserEmail(), false);

        log.trace("Declined online application with ID: " + applicationID);
    }
    public List<OnlineAdoptionApplication> getAllOnlineApplication(){
        return onlineAdoptionApplicationRepository.findAll();
    }

    public OnlineAdoptionApplication findOnlineApplicationByUserIDAndAnimalID(String userID, String animalID) {
        return onlineAdoptionApplicationRepository.findByUserAndAnimal(userID, animalID).orElse(null);
    }
}
