package com.pescue.pescue.service;

import com.pescue.pescue.dto.OTP_DTO;
import com.pescue.pescue.dto.ValidateOTPConfirmEmailDTO;
import com.pescue.pescue.model.OTPConfirmEmail;
import com.pescue.pescue.model.User;
import com.pescue.pescue.repository.OTPConfirmEmailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class OTPService {
    private static final long OTP_EXPIRATION_TIME = 1000 * 60 * 5; //5 phút
    @Autowired
    private UserService userService;
    @Autowired
    private OTPConfirmEmailRepository otpConfirmEmailRepository;
    @Autowired
    private EmailService emailService;

    private final String otpFormat = "000000";

    Logger logger = LoggerFactory.getLogger(OTPService.class);

    public boolean isNumeric(String otp) {
        try {
            Double.parseDouble(otp);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public ResponseEntity<Object> sendOTPConfirmEmail(OTP_DTO request) {
        // Tìm email trong bảng user
        String emailAddress = request.getEmailAddress();
        User user = userService.findUserByUserEmail(emailAddress);

        if (user == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Người dùng không tồn tại");

        // Tạo OTP
        String otp = generateOTP();

        //Nội dung email
        String emailBody = "Mã OTP của bạn là: " + otp;

        // Gửi mail
        boolean emailStatus = emailService.sendMail(emailAddress, emailBody, "Xác nhận tài khoản");

        if (!emailStatus) {
            logger.trace("OTP has been sent to: " + emailAddress);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra khi gửi mail");
        }

        logger.error("There is an error occur when sending OTP to: " + emailAddress);

        // Lưu lịch sử gửi mail vào db
        OTPConfirmEmail otpConfirmEmail = new OTPConfirmEmail(emailAddress,
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis() + OTP_EXPIRATION_TIME),
                otp);

        addOTPConfirmEmail(otpConfirmEmail);

        return ResponseEntity.ok("Đã gửi mail thành công");
    }

    public void addOTPConfirmEmail(OTPConfirmEmail confirmEmail){
        otpConfirmEmailRepository.insert(confirmEmail);
        logger.trace("OTP of " + confirmEmail.getReceiverEmail() +  " has been stored to database");
    }

    public ResponseEntity<Object> validateOTPConfirmEmail(ValidateOTPConfirmEmailDTO request) {

        List<OTPConfirmEmail> otpConfirmEmail = otpConfirmEmailRepository.findOTPConfirmEmailsByReceiverEmailOrderByDate(request.getEmailAddress());

        OTPConfirmEmail recentOtpConfirmEmail = otpConfirmEmail.get(otpConfirmEmail.size() - 1);

        if(request.getOtp().length() != 6 || !isNumeric(request.getOtp())) {
            logger.error("Invalid OTP " + request);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã OTP không hợp lệ");
        }

        if (otpConfirmEmail.isEmpty()) {
            logger.error("Invalid Email Address " + request);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email không hợp lệ");
        }

        if (recentOtpConfirmEmail.getExpiredDate().before(new Date(System.currentTimeMillis()))) {
            logger.error("Expired OTP " + request);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã OTP đã hết hạn");
        }

        if (request.getOtp().equals(recentOtpConfirmEmail.getOTP())) {
            boolean isUnlock = userService.unlockUser(recentOtpConfirmEmail.getReceiverEmail());
            if (!isUnlock) {
                logger.error("Invalid User " + request);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Người dùng không tồn tại");
            }

            logger.error("Valid OTP " + request);
            return ResponseEntity.ok("Mã OTP hợp lệ");
        }

        logger.error("Invalid OTP " + request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã OTP không trùng khớp");

    }

    private String generateOTP() {
        return new DecimalFormat(otpFormat).format(new Random().nextInt(999999));
    }
}
