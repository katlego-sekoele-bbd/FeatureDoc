package com.featuredoc.services;

import com.featuredoc.repository.FeatureStatusRepository;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringJUnitConfig
class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    public void testSendSimpleMessageBestCase() {
        List<String> recipients = List.of(
                "test@test.com",
                "test2@test2.com"
        );
        String subject = "test";
        String text = "text";

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("featuredoc@stocktally.co.za");
        message.setTo(recipients.toArray(new String[0]));
        message.setSubject(subject);
        message.setText(text);

        emailService.sendSimpleMessage(recipients, subject, text);

        verify(emailSender, times(1)).send(message);

    }

    @Test
    public void testSendSimpleMessageNoRecipients() {
        List<String> recipients = List.of();
        String subject = "test";
        String text = "text";

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("featuredoc@stocktally.co.za");
        message.setTo(recipients.toArray(new String[0]));
        message.setSubject(subject);
        message.setText(text);

        emailService.sendSimpleMessage(recipients, subject, text);

        verify(emailSender, times(0)).send(message);
    }

    @Test
    public void testSendSimpleMessageNoSubject() {
        List<String> recipients = List.of(
                "test@test.com",
                "test2@test2.com"
        );
        String subject = "";
        String text = "text";

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("featuredoc@stocktally.co.za");
        message.setTo(recipients.toArray(new String[0]));
        message.setSubject(subject);
        message.setText(text);

        emailService.sendSimpleMessage(recipients, subject, text);

        verify(emailSender, times(1)).send(message);
    }

    @Test
    public void testSendSimpleMessageNoBody() {
        List<String> recipients = List.of(
                "test@test.com",
                "test2@test2.com"
        );
        String subject = "test";
        String text = "";

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("featuredoc@stocktally.co.za");
        message.setTo(recipients.toArray(new String[0]));
        message.setSubject(subject);
        message.setText(text);

        emailService.sendSimpleMessage(recipients, subject, text);

        verify(emailSender, times(1)).send(message);

    }

}