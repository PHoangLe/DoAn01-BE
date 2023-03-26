package com.pescue.pescue.service;

import com.pescue.pescue.repository.OTPConfirmEmailRepository;
import com.pescue.pescue.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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


    public boolean sendMail(String receiverEmail, String emailBody, String subject) {
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(receiverEmail);
            mailMessage.setText(emailBody);
            mailMessage.setSubject(subject);

            javaMailSender.send(mailMessage);

            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}