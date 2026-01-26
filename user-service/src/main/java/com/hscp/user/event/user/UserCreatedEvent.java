package com.hscp.user.event.user;

import com.hscp.user.event.BaseEvent;
import com.hscp.user.event.EventVersion;

import java.time.Instant;
import java.util.UUID;

public record UserCreatedEvent(
        String eventId,
        EventVersion eventVersion,
        Instant occurredAt,

        String userId,
        String name,
        String email
) implements BaseEvent {

    public static UserCreatedEvent of(
            String userId,
            String name,
            String email
    ) {
        return new UserCreatedEvent(
                UUID.randomUUID().toString(),
                EventVersion.V1,
                Instant.now(),
                userId,
                name,
                email
        );
    }
}
