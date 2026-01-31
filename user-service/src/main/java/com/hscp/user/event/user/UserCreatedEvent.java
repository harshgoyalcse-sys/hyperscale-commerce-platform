package com.hscp.user.event.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserCreatedEvent {
    private String userId;
    private String name;
    private String email;
    private Instant createdAt;
}