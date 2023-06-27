package com.pescue.pescue.service;

import com.pescue.pescue.dto.OTP_DTO;
import com.pescue.pescue.dto.ValidateOtpConfirmEmailDTO;
import com.pescue.pescue.dto.ValidateOtpForgotPasswordDTO;
import com.pescue.pescue.exception.*;
import com.pescue.pescue.model.OTPConfirmEmail;
import com.pescue.pescue.model.OTPForgotPassword;
import com.pescue.pescue.model.User;
import com.pescue.pescue.repository.OTPConfirmEmailRepository;
import com.pescue.pescue.repository.OTPForgotPasswordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final OTPForgotPasswordRepository otpForgotPasswordRepository;
    private final EmailService emailService;
    public boolean isNumeric(String otp) {
        try {
            Double.parseDouble(otp);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    public boolean isOtpValid(String otp){
        return otp.length() == 6 && isNumeric(otp);
    }

    private String generateOTP() {
        return new DecimalFormat(OTP_FORMAT).format(new Random().nextInt(999999));
    }


    //Confirm Email
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
        String emailBody = "Mã OTP để xác nhận tài khoản của bạn là: " + otp;

        // Gửi mail
        emailService.sendMail(emailAddress, emailBody, "Xác nhận tài khoản");
        log.trace("Confirm email OTP has been sent to: " + emailAddress);

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

    public void validateOTPConfirmEmail(ValidateOtpConfirmEmailDTO request) throws InvalidException {

        List<OTPConfirmEmail> otpConfirmEmail = otpConfirmEmailRepository.findOTPConfirmEmailsByReceiverEmailOrderByDate(request.getEmailAddress());

        OTPConfirmEmail recentOtpConfirmEmail = otpConfirmEmail.get(otpConfirmEmail.size() - 1);

        if(isOtpValid(recentOtpConfirmEmail.getOTP())) {
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

    //ForgotPassword
    public void sendOTPForgotPassowrd(OTP_DTO request) {
        // Tìm email trong bảng user
        String emailAddress = request.getEmailAddress();
        User user = userService.findUserByUserEmail(emailAddress);

        if (user == null) {
            throw new UserNotFoundException();
        }

        // Tạo OTP
        String otp = generateOTP();

        //Nội dung email
        String emailBody = "Mã OTP đặt lại mật khẩu của bạn là: " + otp;

        // Gửi mail
        emailService.sendMail(emailAddress, emailBody, "Quên mật khẩu");
        log.trace("Forgot password OTP has been sent to: " + emailAddress);

        // Lưu lịch sử gửi mail vào db
        OTPForgotPassword otpForgotPassword = new OTPForgotPassword(emailAddress,
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis() + OTP_EXPIRATION_TIME),
                otp);

        addOTPForgotPassword(otpForgotPassword);
    }
    public void addOTPForgotPassword(OTPForgotPassword forgotPassword){
        otpForgotPasswordRepository.insert(forgotPassword);
        log.trace("OTP forgot Password of " + forgotPassword.getReceiverEmail() +  " has been stored to database");
    }
    public void validateOTPForgotPassword(ValidateOtpForgotPasswordDTO request) throws InvalidException {

        List<OTPForgotPassword> otpForgotPasswords = otpForgotPasswordRepository.findOTPForgotPasswordsByReceiverEmailOrderByDate(request.getEmailAddress());

        OTPForgotPassword recentOtpForgotPassword = otpForgotPasswords.get(otpForgotPasswords.size() - 1);

        if(isOtpValid(recentOtpForgotPassword.getOTP())) {
            throw new InvalidOtpException();
        }

        if (otpForgotPasswords.isEmpty()) {
            throw new InvalidEmailException();
        }

        if (recentOtpForgotPassword.getExpiredDate().before(new Date(System.currentTimeMillis()))) {
            throw new ExpiredOtpException();
        }

        if (!request.getOtp().equals(recentOtpForgotPassword.getOTP())) {
            throw new InvalidOtpException();
        }

        userService.resetPassword(recentOtpForgotPassword.getReceiverEmail(), request.getNewPassword());
    }
}
