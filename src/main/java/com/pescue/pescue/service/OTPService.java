package com.pescue.pescue.service;

import com.pescue.pescue.dto.OTPConfirmEmailDTO;
import com.pescue.pescue.dto.ValidateOTPConfirmEmailDTO;
import com.pescue.pescue.model.OTPConfirmEmail;
import com.pescue.pescue.model.User;
import com.pescue.pescue.repository.OTPConfirmEmailRepository;
import com.pescue.pescue.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

    public boolean isNumeric(String otp) {
        try {
            Double.parseDouble(otp);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public ResponseEntity<String> sendOTPConfirmEmail(OTPConfirmEmailDTO request) {
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

        if (!emailStatus)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra khi gửi mail");

        // Lưu lịch sử gửi mail vào db
        OTPConfirmEmail otpConfirmEmail = new OTPConfirmEmail(emailAddress,
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis() + OTP_EXPIRATION_TIME),
                otp);

        otpConfirmEmailRepository.insert(otpConfirmEmail);

        return ResponseEntity.ok("Đã gửi mail thành công");
    }

    public ResponseEntity<String> validateOTPConfirmEmail(ValidateOTPConfirmEmailDTO request) {

        List<OTPConfirmEmail> otpConfirmEmail = otpConfirmEmailRepository.findOTPConfirmEmailsByReceiverEmailOrderByDate(request.getEmailAddress());

        OTPConfirmEmail recentOtpConfirmEmail = otpConfirmEmail.get(otpConfirmEmail.size() - 1);

        if(request.getOtp().length() != 6 || !isNumeric(request.getOtp()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã OTP không hợp lệ");

        if (otpConfirmEmail.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email không hợp lệ");

        if (recentOtpConfirmEmail.getExpiredDate().before(new Date(System.currentTimeMillis())))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã OTP đã hết hạn");

        if (request.getOtp().equals(recentOtpConfirmEmail.getOTP())) {
            boolean isUnlock = userService.unlockUser(recentOtpConfirmEmail.getReceiverEmail());
            if (!isUnlock)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Người dùng không tồn tại");

            return ResponseEntity.ok("Mã OTP hợp lệ");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã OTP không trùng khớp");

    }

    private String generateOTP() {
        return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }
}
