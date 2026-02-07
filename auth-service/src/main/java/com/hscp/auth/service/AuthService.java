package com.hscp.auth.service;

import com.hscp.auth.dto.LoginRequest;
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
    private final JwtService jwtService;

    public void register(RegisterRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        if (repository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }

        AuthUser user = new AuthUser(
                null,
                email,
                passwordEncoder.encode(request.getPassword()),
                "USER"
        );

        repository.save(user);
    }

    public String login(LoginRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        AuthUser user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        // 🔑 JWT subject = userId only
        return jwtService.generateToken(
                user.getId().toString(),
                user.getRole()
        );
    }
}
