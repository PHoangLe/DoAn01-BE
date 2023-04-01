package com.pescue.pescue.service;

import com.pescue.pescue.model.Role;
import com.pescue.pescue.model.Shelter;
import com.pescue.pescue.repository.ShelterRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class ShelterService {
    @Autowired
    ShelterRepository shelterRepository;
    @Autowired
    UserService userService;
    @Autowired
    EmailService emailService;
    Logger logger = LoggerFactory.getLogger(ShelterService.class);

    public Shelter findShelterByUserID(String userID){
        if (shelterRepository.findShelterByUserID(userID).isPresent()) {
            logger.trace("Found shelter with userID: " + userID);
            return shelterRepository.findShelterByUserID(userID).get();
        }
        logger.trace("Can't find shelter with userID: " + userID);
        return null;
    }

    public Shelter findShelterByShelterID(String shelterID){
        if (shelterRepository.findShelterByShelterID(shelterID).isPresent()) {
            logger.trace("Found shelter with shelterID: " + shelterID);
            return shelterRepository.findShelterByShelterID(shelterID).get();
        }
        logger.trace("Can't find shelter with shelterID: " + shelterID);
        return null;
    }

    public boolean updateShelter(Shelter shelter){
        try {
            shelterRepository.save(shelter);
        }
        catch (Exception e){
            logger.error("There is an error occur while saving shelter information: " + shelter);
            return false;
        }
        logger.trace("Shelter information has been saved in the database: " + shelter);
        return true;
    }

    public ResponseEntity<Object> registerShelter(Shelter shelter){
        if (userService.findUserByID(shelter.getUserID()) == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tài khoản đăng ký làm trại cứu trợ không tồn tại");

        if(!addShelter(shelter)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Đã có lỗi khi thêm thông tin trại cứu trợ vào cơ sở dữ liệu");
        }

        logger.trace("Shelter information has been inserted in the database: " + shelter);

        return ResponseEntity.status(HttpStatus.CREATED).body("Bạn đã gửi yêu cầu làm trại cứu hộ thành công. Vui lòng đợi 3-5 ngày để nhận được kết quả đăng ký");
    }

    public boolean addShelter(Shelter shelter){
        try {
            shelterRepository.insert(shelter);
        }
        catch (Exception e){
            logger.error("There is an error occur while adding shelter information to database: " + shelter);
            return false;
        }
        return true;
    }

    public List<Shelter> findAllShelter(){
        return shelterRepository.findAll();
    }

    public ResponseEntity<Object> approveShelter(String shelterID) {
        Shelter shelter = findShelterByShelterID(shelterID);

        if (shelter == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không tồn tại trại cứu trợ");

        shelter.setApproved(true);

        if(updateShelter(shelter) && sendNotifyEmail(shelter)) {
            if (!userService.addRoleForUser(shelter.getUserID(), Role.ROLE_SHELTER_MANAGER)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra khi thêm quyền cho người dùng");
            }

            logger.trace("Added ROLE_SHELTER_MANAGER for user with Id: " + shelter.getUserID());
            return ResponseEntity.ok("Đã xác thực yêu cầu làm trại cứu trợ thành công");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Đã có lỗi xảy ra vui lòng thử lại sau");
    }

    private boolean sendNotifyEmail(Shelter shelter) {
        return emailService.sendMail(shelter.getRepresentativeEmailAddress(),
                "Đăng ký làm trại cứu trợ của bạn đã được chấp thuận. Tài khoản của bạn đã trở thành tài khoản của trại cứu trợ. Chào mừng bạn đến với Pescue!",
                "Kết quả đăng ký làm trại cứu hộ");
    }
}
