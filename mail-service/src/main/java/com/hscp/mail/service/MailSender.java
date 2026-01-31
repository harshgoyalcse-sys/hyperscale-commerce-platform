package com.hscp.mail.service;

import com.hscp.mail.event.user.UserCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MailSender {

    public void sendWelcomeMail(UserCreatedEvent event) {
        log.info("📧 Sending welcome mail to {}", event.getEmail());
    }
}