package com.hscp.user.service;

import com.hscp.user.domain.User;
import com.hscp.user.entity.UserEntity;
import com.hscp.user.exception.UserNotFoundException;
import com.hscp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MailService mailService;

    @Transactional
    public User createUser(String name, String email) {

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        String id = UUID.randomUUID().toString();
        Instant now = Instant.now();

        UserEntity entity = new UserEntity(
                id,
                name,
                email,
                now
        );

        userRepository.save(entity);
        // SIDE EFFECT
        mailService.sendWelcomeMail(email, name);

        return new User(id, name, email, now);
    }

    @Transactional(readOnly = true)
    public User getUser(String id) {

        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getCreatedAt()
        );
    }
}
