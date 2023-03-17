package com.pescue.pescue.service;

import com.pescue.pescue.model.EmailDetails;
import com.pescue.pescue.model.OTPConfirmEmail;
import com.pescue.pescue.model.User;
import com.pescue.pescue.repository.OTPConfirmEmailRepository;
import com.pescue.pescue.repository.UserRepository;
import com.pescue.pescue.requestbody.OTPConfirmEmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;
    @Autowired
    private OTPConfirmEmailRepository otpConfirmEmailRepository;
    @Autowired
    private UserRepository userRepository;


    public String sendOTPConfirmEmail(OTPConfirmEmailRequest request) {
        // Tìm email trong bảng user
        String emailAddress = request.getEmailAddress();
        Optional<User> user = userRepository.findUserByUserEmail(emailAddress);

        if (user.isEmpty())
            return "Người dùng không tồn tại";

        try {

            // Khởi tạo mail
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();

            // Tạo OTP
            String otp = generateOTP();

            //Nội dung email
            String emailBody = "Mã OTP của bạn là: " + otp;

            // Các thông tin liên quan
            mailMessage.setFrom(sender);
            mailMessage.setTo(emailAddress);
            mailMessage.setText(emailBody);
            mailMessage.setSubject("Xác nhận tài khoản");

            // Gửi mail
            javaMailSender.send(mailMessage);

            // Lưu lịch sử gửi mail vào db
            OTPConfirmEmail otpConfirmEmail =  new OTPConfirmEmail(emailAddress, new Date(System.currentTimeMillis()), otp);
            otpConfirmEmailRepository.insert(otpConfirmEmail);

            return "Đã gửi mail thành công";
        }

        catch (Exception e) {
            System.out.println(e);
            return "Có lỗi đã xảy ra khi gửi mail";
        }
    }

    private String generateOTP() {
        return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }
}