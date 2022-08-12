package com.laiandlina.crm.domain.service;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.mail.*;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.*;

@Service
public class EmailService{
static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String subject,
                          String body)  {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("nonreplylaiandlina@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);

    }

}