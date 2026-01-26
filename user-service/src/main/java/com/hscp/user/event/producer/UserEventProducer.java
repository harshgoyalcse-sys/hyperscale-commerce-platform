package com.hscp.user.event.producer;

import com.hscp.user.event.user.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventProducer {

    private static final String USER_CREATED_TOPIC = "user-created";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishUserCreated(UserCreatedEvent event) {
        kafkaTemplate.send(USER_CREATED_TOPIC, event.userId(), event);
        log.info("Published UserCreatedEvent for userId={}", event.userId());
    }
}
