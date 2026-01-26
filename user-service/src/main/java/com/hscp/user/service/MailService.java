package com.hscp.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendWelcomeMail(String to, String name) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Welcome to HSCP 🎉");
        message.setText(
                "Hi " + name + ",\n\n" +
                        "Welcome to Hyperscale Commerce Platform.\n\n" +
                        "— HSCP Team"
        );

        mailSender.send(message);

        log.info("Welcome email sent to {}", to);
    }
}
