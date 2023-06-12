package com.pescue.pescue.service;

import com.pescue.pescue.exception.ExistedException;
import com.pescue.pescue.exception.ShelterNotFoundException;
import com.pescue.pescue.exception.UserNotFoundException;
import com.pescue.pescue.model.Shelter;
import com.pescue.pescue.repository.ShelterRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ShelterService {
    private final ShelterRepository shelterRepository;
    private final UserService userService;
    private final EmailService emailService;
    Logger logger = LoggerFactory.getLogger(ShelterService.class);

    public Shelter findShelterByUserID(String userID){
        return shelterRepository.findShelterByUserID(userID).orElse(null);
    }

    public Shelter getShelterByShelterID(String shelterID){
        return shelterRepository.findShelterByShelterID(shelterID).orElse(null);
    }

    public boolean updateShelter(Shelter shelter){
        try {
            shelterRepository.save(shelter);
        }
        catch (Exception e){
            logger.error("There is an error occur while saving shelter information: " + shelter);
            return false;
        }
        logger.trace("Shelter information have been saved in the database: " + shelter);
        return true;
    }

    public void registerShelter(Shelter shelter) throws ExistedException {
        if (userService.getUserByID(shelter.getUserID()) == null)
            throw new UserNotFoundException();

        Shelter tempShelter = findShelterByUserID(shelter.getUserID());

        if (tempShelter != null)
            throw new ExistedException("Tài khoản này đã là tài khoản Shelter hoặc đang trong quá trình xác thực bởi ban quan lý.");

        shelterRepository.insert(shelter);
        logger.trace("Shelter information have been inserted in the database: " + shelter);
    }

    public List<Shelter> findAllShelter(){
        return shelterRepository.findShelterByIsApproved(true);

    }
    public List<Shelter> findShelterToApprove(){
        return shelterRepository.findShelterByIsApproved(false);
    }
    public void approveShelter(String shelterID) {
        Shelter shelter = getShelterByShelterID(shelterID);

        if (shelter == null)
            throw new ShelterNotFoundException();

        shelter.setApproved(true);

        updateShelter(shelter);
        sendNotifyEmail(shelter, true);

        logger.trace("Added ROLE_SHELTER_MANAGER for user with Id: " + shelter.getUserID());
    }
    private void sendNotifyEmail(Shelter shelter, boolean isApprove) {
        String emailBody;
        if (isApprove){
            emailBody = "Đăng ký làm trại cứu trợ của bạn đã được chấp thuận. Tài khoản của bạn đã trở thành tài khoản của trại cứu trợ. Chào mừng bạn đến với Pescue!";
        }
        else {
            emailBody = """
                    Chúng tôi rất tiếc khi phải thông báo rằng yêu cầu làm trại cứu trợ của bạn đã bị từ chối.
                    Để biết thêm thông tin chi tiết xin vui lòng liên lạc lại với chúng tôi
                    Bạn có thể gửi lại yêu cầu với đầy đủ thông tin hơn.""";
        }

        emailService.sendMail(
                shelter.getRepresentativeEmailAddress(),
                emailBody,
                "Kết quả đăng ký làm trại cứu hộ");
    }

    public Shelter findShelterByShelterName(String shelterName) {
        return shelterRepository.findShelterByShelterName(shelterName).orElse(null);
    }

    public void deleteShelter(Shelter shelter){
        try {
            shelterRepository.delete(shelter);
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    public void disapproveShelter(String shelterID) {
        Shelter shelter = getShelterByShelterID(shelterID);

        if (shelter == null)
            throw new ShelterNotFoundException();

        shelterRepository.delete(shelter);

        deleteShelter(shelter);
        sendNotifyEmail(shelter, false);
    }
}
