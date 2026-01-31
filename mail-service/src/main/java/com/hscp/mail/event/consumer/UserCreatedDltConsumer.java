package com.hscp.mail.event.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserCreatedDltConsumer {

    @KafkaListener(topics = "user.created.DLT")
    public void consumeDlt(String message) {
        log.error("☠️ Poison message moved to DLT: {}", message);
    }
}