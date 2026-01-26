package com.hscp.user.event;

import java.time.Instant;

public interface BaseEvent {

    String eventId();

    EventVersion eventVersion();

    Instant occurredAt();
}
