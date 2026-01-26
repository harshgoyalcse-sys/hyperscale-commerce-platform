package com.hscp.user.service;

import com.hscp.user.domain.User;
import com.hscp.user.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class UserService {

    private final Map<String, User> store = new ConcurrentHashMap<>();

    public User createUser(String name, String email) {
        log.info("Creating user with email={}", email);

        String id = UUID.randomUUID().toString();
        User user = new User(id, name, email, Instant.now());

        store.put(id, user);
        return user;
    }

    public User getUser(String id) {
        return store.computeIfAbsent(id, key -> {
            throw new UserNotFoundException(id);
        });
    }
}
