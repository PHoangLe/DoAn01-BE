package com.pescue.pescue.service;

import com.pescue.pescue.exception.SendMailFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EmailService {
    private final JavaMailSender javaMailSender;
    @Value("${MAIL_USERNAME}")
    private String sender;

    public void sendMail(String receiverEmail, String emailBody, String subject) {
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(receiverEmail);
            mailMessage.setText(emailBody);
            mailMessage.setSubject(subject);

            javaMailSender.send(mailMessage);

            log.trace("Mail has been sent to: " + receiverEmail);

        }
        catch (Exception e){
            throw new SendMailFailedException();
        }
    }
}