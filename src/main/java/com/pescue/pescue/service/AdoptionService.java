package com.pescue.pescue.service;

import com.pescue.pescue.dto.AdoptionApplicationRequestDTO;
import com.pescue.pescue.exception.*;
import com.pescue.pescue.model.*;
import com.pescue.pescue.model.constant.ApplicationStatus;
import com.pescue.pescue.model.constant.TransactionType;
import com.pescue.pescue.repository.AdoptionApplicationRepository;
import com.pescue.pescue.repository.OnlineAdoptionApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdoptionService {
    private final static String GENERAL_FUND_ID = "646aed7fc03b151c35ce8d1b";
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
    public void createAdoptionRequest(AdoptionApplicationRequestDTO dto) throws ExistedException {
        AdoptionApplication existedApplication = findApplicationByUserIDAndAnimalID(dto.getUserID(), dto.getAnimalID());
        if(existedApplication != null && existedApplication.getApplicationStatus() != ApplicationStatus.REJECTED){
            throw new ExistedException("Đã tồn tại yêu cầu nhận nuôi");
        }

        User user = userService.getUserByID(dto.getUserID());
        if (user == null) {
            log.trace("User not found ID: " + dto.getUserID());
            throw new UserNotFoundException();
        }

        Animal animal = animalService.getAnimalByAnimalID(dto.getAnimalID());
        if (animal == null) {
            log.trace("Animal not found ID: " + dto.getAnimalID());
            throw new AnimalNotFoundException();
        }

        Shelter shelter = shelterService.getShelterByShelterID(dto.getShelterID());
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
        return adoptionApplicationRepository.findByApplicationID(applicationID).orElseThrow(ApplicationNotFoundException::new);
    }

    public void confirmAdoptionRequest(String applicationID) throws ExistedException {
        AdoptionApplication application = findApplicationByApplicationID(applicationID);

        application.setApplicationStatus(ApplicationStatus.COMPLETED);
        adoptionApplicationRepository.save(application);
        Animal animal = animalService.getAnimalByAnimalID(application.getAnimal().getAnimalID());
        animal.setAdopted(true);
        animalService.updateAnimal(animal);

        User user = userService.getUserByID(application.getUser().getUserID());
        sendResultEmail(user.getUserEmail(), true);

        log.trace("Approved application with ID: " + applicationID);
    }
    public void declineAdoptionRequest(String applicationID) {
        AdoptionApplication application = findApplicationByApplicationID(applicationID);

        application.setApplicationStatus(ApplicationStatus.REJECTED);
        adoptionApplicationRepository.save(application);

        User user = userService.getUserByID(application.getUser().getUserID());
        sendResultEmail(user.getUserEmail(), false);

        log.trace("Declined application with ID: " + applicationID);
    }
    public List<AdoptionApplication> getApplicationByShelterID(String shelterID) {
        Shelter shelterByShelterID = shelterService.getShelterByShelterID(shelterID);

        if (shelterByShelterID == null){
            log.trace("Shelter not found ID: " + shelterID);
            throw new ShelterNotFoundException();
        }

        return adoptionApplicationRepository.findAllByShelter(shelterID);
    }
    public AdoptionApplication findApplicationByUserIDAndAnimalID(String userID, String animalID) {
        return adoptionApplicationRepository.findByUserAndAnimal(userID, animalID).orElse(null);
    }
    public List<AdoptionApplication> getAllApplication() {
        return adoptionApplicationRepository.findAll();
    }


    //Online Adoption
    public void createOnlineAdoptionRequest(AdoptionApplicationRequestDTO dto) throws ExistedException {
        OnlineAdoptionApplication existedApplication = getOnlineApplicationByUserIDAndAnimalID(dto.getUserID(), dto.getAnimalID());
        if(existedApplication != null){
            if (existedApplication.getApplicationStatus() == ApplicationStatus.PENDING) {
                log.trace("Application already existed: User " + existedApplication.getUser().getUserID() + " Animal " + existedApplication.getAnimal().getAnimalID());
                throw new ExistedException("Bạn đã gửi yêu cầu nhận nuôi rồi vui lòng đợi chúng tôi xác nhận");
            }
            if (existedApplication.getApplicationStatus() == ApplicationStatus.COMPLETED){
                existedApplication.setApplicationStatus(ApplicationStatus.EXTENDING);
                updateOnlineAdoptionApplication(existedApplication);
                return;
            }
        }
        User user = userService.getUserByID(dto.getUserID());
        if (user == null) {
            throw new UserNotFoundException();
        }
        Animal animal = animalService.getAnimalByAnimalID(dto.getAnimalID());
        if (animal == null) {
            throw new AnimalNotFoundException();
        }
        Shelter shelter = shelterService.getShelterByShelterID(dto.getShelterID());
        if (shelter == null) {
            throw new ShelterNotFoundException();
        }

        OnlineAdoptionApplication application = new OnlineAdoptionApplication(animal, shelter, user);

        onlineAdoptionApplicationRepository.insert(application);
        log.trace("added online adoption application for user: " + application.getUser().getUserID() + " pet: " + application.getAnimal().getAnimalID());

    }
    public OnlineAdoptionApplication findOnlineApplicationByApplicationID(String applicationID){
        return onlineAdoptionApplicationRepository.findByApplicationID(applicationID).orElse(null);
    }
    @Transactional
    public void confirmOnlineAdoptionRequest(String applicationID) throws UpdateFundException, ApplicationStatusUpdateException, ExistedException {
        OnlineAdoptionApplication application = findOnlineApplicationByApplicationID(applicationID);

        if (application == null) {
            throw new ApplicationNotFoundException();
        }

        if (application.getApplicationStatus() == ApplicationStatus.COMPLETED){
            throw new ApplicationStatusUpdateException("Yêu cầu đã được chấp thuận rồi");
        }

        if (application.getApplicationStatus() == ApplicationStatus.EXTENDING){
            extendingOnlineAdoption(application);
            return;
        }

        User user = userService.getUserByID(application.getUser().getUserID());

        fundTransactionService.createTransaction(TransactionType.USER_TO_FUND, GENERAL_FUND_ID, user, new BigDecimal(120_000));

        application.setApplicationStatus(ApplicationStatus.COMPLETED);
        application = setExpiry(application);
        onlineAdoptionApplicationRepository.save(application);

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
    @Transactional
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

        User user = userService.getUserByID(application.getUser().getUserID());
        sendResultEmail(user.getUserEmail(), false);

        log.trace("Declined online application with ID: " + applicationID);
    }
    public List<OnlineAdoptionApplication> getAllOnlineApplication(){
        return onlineAdoptionApplicationRepository.findAll();
    }

    public OnlineAdoptionApplication getOnlineApplicationByUserIDAndAnimalID(String userID, String animalID) {
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
    public List<OnlineAdoptionApplication> getOnlineAdoptionsByUserID(String userID) {
        List<OnlineAdoptionApplication> allByUser = onlineAdoptionApplicationRepository.findAllByUser(userID);
        return allByUser.stream()
                .filter((application) -> application.getApplicationStatus() == ApplicationStatus.COMPLETED)
                .toList();
    }
}
