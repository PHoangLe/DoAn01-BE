package com.pescue.pescue.service;

import com.pescue.pescue.dto.OTP_DTO;
import com.pescue.pescue.dto.StringResponseDTO;
import com.pescue.pescue.dto.ValidateOTPConfirmEmailDTO;
import com.pescue.pescue.exception.*;
import com.pescue.pescue.model.OTPConfirmEmail;
import com.pescue.pescue.model.User;
import com.pescue.pescue.repository.OTPConfirmEmailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OTPService {
    private static final long OTP_EXPIRATION_TIME = 1000 * 60 * 5; //5 phút
    private static final String OTP_FORMAT = "000000";
    private final UserService userService;
    private final OTPConfirmEmailRepository otpConfirmEmailRepository;
    private final EmailService emailService;
    public boolean isNumeric(String otp) {
        try {
            Double.parseDouble(otp);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public void sendOTPConfirmEmail(OTP_DTO request) {
        // Tìm email trong bảng user
        String emailAddress = request.getEmailAddress();
        User user = userService.findUserByUserEmail(emailAddress);

        if (user == null) {
            throw new UserNotFoundException();
        }

        // Tạo OTP
        String otp = generateOTP();

        //Nội dung email
        String emailBody = "Mã OTP của bạn là: " + otp;

        // Gửi mail
        emailService.sendMail(emailAddress, emailBody, "Xác nhận tài khoản");
        log.trace("OTP has been sent to: " + emailAddress);

        // Lưu lịch sử gửi mail vào db
        OTPConfirmEmail otpConfirmEmail = new OTPConfirmEmail(emailAddress,
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis() + OTP_EXPIRATION_TIME),
                otp);

        addOTPConfirmEmail(otpConfirmEmail);
    }

    public void addOTPConfirmEmail(OTPConfirmEmail confirmEmail){
        otpConfirmEmailRepository.insert(confirmEmail);
        log.trace("OTP of " + confirmEmail.getReceiverEmail() +  " has been stored to database");
    }

    public void validateOTPConfirmEmail(ValidateOTPConfirmEmailDTO request) throws InvalidException {

        List<OTPConfirmEmail> otpConfirmEmail = otpConfirmEmailRepository.findOTPConfirmEmailsByReceiverEmailOrderByDate(request.getEmailAddress());

        OTPConfirmEmail recentOtpConfirmEmail = otpConfirmEmail.get(otpConfirmEmail.size() - 1);

        if(request.getOtp().length() != 6 || !isNumeric(request.getOtp())) {
            throw new InvalidOtpException();
        }

        if (otpConfirmEmail.isEmpty()) {
            throw new InvalidEmailException();
        }

        if (recentOtpConfirmEmail.getExpiredDate().before(new Date(System.currentTimeMillis()))) {
            throw new ExpiredOtpException();
        }

        if (!request.getOtp().equals(recentOtpConfirmEmail.getOTP())) {
            throw new InvalidOtpException();
        }

        userService.unlockUser(recentOtpConfirmEmail.getReceiverEmail());
    }

    private String generateOTP() {
        return new DecimalFormat(OTP_FORMAT).format(new Random().nextInt(999999));
    }
}
