package com.hscp.mail.event.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hscp.mail.event.user.UserCreatedEvent;
import com.hscp.mail.service.MailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCreatedConsumer {

    private final ObjectMapper objectMapper;
    private final MailSender mailSender;

    @KafkaListener(topics = "user.created")
    public void consume(String message) throws Exception {

        UserCreatedEvent event =
                objectMapper.readValue(message, UserCreatedEvent.class);

        log.info("User created event received: {}", event);

        mailSender.sendWelcomeMail(event);
    }
}