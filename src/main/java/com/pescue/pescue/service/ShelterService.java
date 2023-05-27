package com.pescue.pescue.service;

import com.pescue.pescue.dto.StringResponseDTO;
import com.pescue.pescue.model.Role;
import com.pescue.pescue.model.Shelter;
import com.pescue.pescue.repository.ShelterRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
@RequiredArgsConstructor
public class ShelterService {
    private final ShelterRepository shelterRepository;
    private final UserService userService;
    private final EmailService emailService;
    Logger logger = LoggerFactory.getLogger(ShelterService.class);

    public Shelter findShelterByUserID(String userID){
        if (shelterRepository.findShelterByUserID(userID).isPresent()) {
            logger.trace("Found shelter with userID: " + userID);
            return shelterRepository.findShelterByUserID(userID).get();
        }
        logger.trace("Can't find any shelter with userID: " + userID);
        return null;
    }

    public Shelter findShelterByShelterID(String shelterID){
        if (shelterRepository.findShelterByShelterID(shelterID).isPresent()) {
            logger.trace("Found shelter with shelterID: " + shelterID);
            return shelterRepository.findShelterByShelterID(shelterID).get();
        }
        logger.trace("Can't find any shelter with shelterID: " + shelterID);
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
        logger.trace("Shelter information have been saved in the database: " + shelter);
        return true;
    }

    public ResponseEntity<Object> registerShelter(Shelter shelter){
        if (userService.findUserByID(shelter.getUserID()) == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                    .message("Tài khoản đăng ký làm trại cứu trợ không tồn tại")
                    .build());

        if(!addShelter(shelter)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                    .message("Đã có lỗi khi thêm thông tin trại cứu trợ vào cơ sở dữ liệu")
                    .build());
        }

        logger.trace("Shelter information have been inserted in the database: " + shelter);

        return ResponseEntity.status(HttpStatus.CREATED).body(StringResponseDTO.builder()
                .message("Bạn đã gửi yêu cầu làm trại cứu hộ thành công. Vui lòng đợi 3-5 ngày để nhận được kết quả đăng ký")
                .build());
    }

    public boolean addShelter(Shelter shelter){
        try {
            shelterRepository.insert(shelter);
        }
        catch (Exception e){
            logger.error("There is an error occur while inserting shelter information to database: " + shelter);
            return false;
        }
        return true;
    }

    public List<Shelter> findAllShelter(){
        return shelterRepository.findShelterByIsApproved(true);

    }public List<Shelter> findShelterToApprove(){
        return shelterRepository.findShelterByIsApproved(false);
    }

    public ResponseEntity<Object> approveShelter(String shelterID) {
        Shelter shelter = findShelterByShelterID(shelterID);

        if (shelter == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                    .message("Không tồn tại trại cứu trợ")
                    .build());

        shelter.setApproved(true);

        if(updateShelter(shelter) && sendNotifyEmail(shelter, true)) {
            if (!userService.addRoleForUser(shelter.getUserID(), Role.ROLE_SHELTER_MANAGER)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                        .message("Có lỗi xảy ra khi thêm quyền cho người dùng")
                        .build());
            }

            logger.trace("Added ROLE_SHELTER_MANAGER for user with Id: " + shelter.getUserID());
            return ResponseEntity.ok(StringResponseDTO.builder()
                    .message("Đã xác thực yêu cầu làm trại cứu trợ thành công")
                    .build());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                .message("Đã có lỗi xảy ra vui lòng thử lại sau")
                .build());
    }

    private boolean sendNotifyEmail(Shelter shelter, boolean isApprove) {
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

        return emailService.sendMail(shelter.getRepresentativeEmailAddress(),
                emailBody,
                "Kết quả đăng ký làm trại cứu hộ");
    }

    public Shelter findShelterByShelterName(String shelterName) {
        if (shelterRepository.findShelterByShelterName(shelterName).isPresent()) {
            logger.trace("Found shelter with shelterName: " + shelterName);
            return shelterRepository.findShelterByShelterName(shelterName).get();
        }
        logger.trace("Can't find any shelter with shelterName: " + shelterName);
        return null;
    }

    public boolean deleteShelter(Shelter shelter){
        try {
            shelterRepository.delete(shelter);
            return true;
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
    }

    public ResponseEntity<Object> disapproveShelter(String shelterID) {
        Shelter shelter = findShelterByShelterID(shelterID);

        if (shelter == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                    .message("Không tồn tại trại cứu trợ")
                    .build());

        shelterRepository.delete(shelter);

        if(deleteShelter(shelter) && sendNotifyEmail(shelter, false)) {
            logger.trace("Deleted shelter: " + shelterID);
            return ResponseEntity.ok(StringResponseDTO.builder()
                    .message("Đã từ chối yêu cầu làm trại cứu trợ thành công")
                    .build());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                .message("Đã có lỗi xảy ra vui lòng thử lại sau")
                .build());
    }
}
