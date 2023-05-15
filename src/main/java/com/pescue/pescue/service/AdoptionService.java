package com.pescue.pescue.service;

import com.pescue.pescue.dto.AdoptionApplicationDTO;
import com.pescue.pescue.dto.AdoptionApplicationRequestDTO;
import com.pescue.pescue.exception.*;
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
    private final EmailService emailService;

    private void sendResultEmail(String receiverEmail, boolean isConfirm) throws SendMailFailedException {
        String emailBody;
        String subject = "Nhận nuôi";

        if (isConfirm){
            emailBody = """
                    Chúc mừng bạn,
                    Yêu cầu nhận nuôi của bạn đã được xử lý xong.
                    Chúng tôi đại diện trại cứu trợ và bé được bạn nhận nuôi gửi đến bạn một lời cảm ơn chân thành.
                    Nếu có bất cứ thắc mắc nào hãy liên hệ lại với chúng thôi qua email này.
                    Pescue.""";
        }else {
            emailBody = """
                    Chào bạn,
                    Yêu cầu nhận nuôi của bạn đã được xử lý xong.
                    Nhưng chúng tôi lấy làm tiếc về việc nhận nuôi của bạn không thành công.
                    Nếu có bất cứ thắc mắc nào hãy liên hệ lại với chúng thôi qua email này.
                    Pescue.""";
        }
        emailService.sendMail(receiverEmail, emailBody, subject);
    }
    //Offline Adoption
    public void createAdoptionRequest(AdoptionApplicationRequestDTO dto){
        AdoptionApplication application = new AdoptionApplication(dto);

        System.out.println(dto);

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

        User user = userService.findUserByID(application.getUserID());
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

        User user = userService.findUserByID(application.getUserID());
        sendResultEmail(user.getUserEmail(), false);

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

        User user = userService.findUserByID(application.getUserID());
        sendResultEmail(user.getUserEmail(), false);

        log.trace("Declined online application with ID: " + applicationID);
    }
    public List<OnlineAdoptionApplication> getAllOnlineApplication(){
        return onlineAdoptionApplicationRepository.findAll();
    }
}
