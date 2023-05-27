package com.pescue.pescue.service;

import com.pescue.pescue.dto.AdoptionApplicationRequestDTO;
import com.pescue.pescue.exception.*;
import com.pescue.pescue.model.*;
import com.pescue.pescue.repository.AdoptionApplicationRepository;
import com.pescue.pescue.repository.OnlineAdoptionApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final FundTransactionService fundTransactionService;

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
            if (existedApplication.getApplicationStatus() == ApplicationStatus.COMPLETED){
                existedApplication.setApplicationStatus(ApplicationStatus.EXTENDING);
                updateOnlineAdoptionApplication(existedApplication);
                return;
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
    public void confirmOnlineAdoptionRequest(String applicationID) throws UpdateFundException, ApplicationStatusException {
        OnlineAdoptionApplication application = findOnlineApplicationByApplicationID(applicationID);

        if (application == null) {
            log.trace("Can't find application with ID: " + applicationID);
            throw new ApplicationNotFoundException();
        }

        if (application.getApplicationStatus() == ApplicationStatus.COMPLETED){
            log.trace("Application is already completed");
            throw new ApplicationStatusException("Yêu cầu đã được chấp thuận rồi");
        }

        if (application.getApplicationStatus() == ApplicationStatus.EXTENDING){
            extendingOnlineAdoption(application);
            return;
        }

        Animal animal = animalService.findAnimalByAnimalID(application.getAnimal().getAnimalID());
        User user = userService.findUserByID(application.getUser().getUserID());

        fundTransactionService.createTransaction(TransactionType.USER_TO_FUND, user.getUserID(), "646aed7fc03b151c35ce8d1b", new BigDecimal(120_000));

        application.setApplicationStatus(ApplicationStatus.COMPLETED);
        application = setExpiry(application);
        onlineAdoptionApplicationRepository.save(application);
        animalService.addOnlineAdopters(animal, user);

        sendResultEmail(user.getUserEmail(), true);

        log.trace("Approved online application with ID: " + applicationID);

    }
    private void extendingOnlineAdoption(OnlineAdoptionApplication application) {
        application.setApplicationStatus(ApplicationStatus.COMPLETED);

        Calendar cal = Calendar.getInstance();
        cal.setTime(application.getExpiry());
        cal.add(Calendar.MONTH, 1);
        application.setExpiry(cal.getTime());

        updateOnlineAdoptionApplication(application);
        sendResultEmail(application.getUser().getUserEmail(), true);
    }
    public void declineOnlineAdoptionRequest(String applicationID) {
        OnlineAdoptionApplication application = findOnlineApplicationByApplicationID(applicationID);

        if (application == null) {
            log.trace("Can't find application with ID: " + applicationID);
            throw new ApplicationNotFoundException();
        }

        if (application.getApplicationStatus() == ApplicationStatus.EXTENDING){
            application.setApplicationStatus(ApplicationStatus.COMPLETED);
            updateOnlineAdoptionApplication(application);
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
    public List<OnlineAdoptionApplication> getAllOnlineApplicationByApplicationStatus(ApplicationStatus status){
        return onlineAdoptionApplicationRepository.findAllByApplicationStatus(status);
    }
    public OnlineAdoptionApplication setExpiry(OnlineAdoptionApplication onlineAdoptionApplication){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        cal.add(Calendar.MONTH, 1);
        onlineAdoptionApplication.setExpiry(cal.getTime());
        return onlineAdoptionApplication;
    }
    public void updateOnlineAdoptionApplication(OnlineAdoptionApplication application){
        onlineAdoptionApplicationRepository.save(application);
        log.trace("Updated status Online Application: " + application.getApplicationID());
    }
    public void checkExpiryOnlineAdoption() {
        log.trace("Daily check for online adoptions started");
        List<OnlineAdoptionApplication> onlineAdoptionApplications = getAllOnlineApplicationByApplicationStatus(ApplicationStatus.COMPLETED);

        List<OnlineAdoptionApplication> collect = onlineAdoptionApplications.stream()
                .filter((application) -> application.getExpiry().before(new Date(System.currentTimeMillis())))
                .toList();

        collect.forEach(application -> {
            application.setApplicationStatus(ApplicationStatus.EXPIRED);
            updateOnlineAdoptionApplication(application);
            animalService.removeOnlineAdopters(application.getAnimal(), application.getUser());
        });
        log.trace("Daily check for online adoptions ended");
    }
}
