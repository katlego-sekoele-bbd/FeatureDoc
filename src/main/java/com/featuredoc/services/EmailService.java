package com.featuredoc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(
            String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("featuredoc@stocktally.co.za");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendSimpleMessage(List<String> to, String subject, String text) {
        if (!to.isEmpty() && !(subject.isEmpty() && text.isEmpty())) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("featuredoc@stocktally.co.za");
            message.setTo(to.toArray(new String[0]));
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
        } else {
            // There is no list of recipients to send the email to, no need to do all the work.
        }
    }
}
