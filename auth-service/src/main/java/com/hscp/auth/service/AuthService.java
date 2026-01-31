package com.hscp.auth.service;

import com.hscp.auth.dto.RegisterRequest;
import com.hscp.auth.entity.AuthUser;
import com.hscp.auth.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {

        if (repository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        AuthUser user = new AuthUser(
                null,
                request.getEmail().toLowerCase(),
                passwordEncoder.encode(request.getPassword()),
                "USER"
        );

        repository.save(user);
    }
}