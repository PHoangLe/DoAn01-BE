package com.pescue.pescue.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    @Value("${MAIL_USERNAME}")
    private String sender;

    Logger logger = LoggerFactory.getLogger(EmailService.class);

    public boolean sendMail(String receiverEmail, String emailBody, String subject) {
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(receiverEmail);
            mailMessage.setText(emailBody);
            mailMessage.setSubject(subject);

            javaMailSender.send(mailMessage);

            logger.trace("Mail has been sent to: " + receiverEmail);

            return true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}