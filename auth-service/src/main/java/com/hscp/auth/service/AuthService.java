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

    public String login(LoginRequest request){
        AuthUser user = repository.findByEmail(request.getEmail().toLowerCase()).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if(!passwordEncoder.matches(request.getPassword() , user.getPasswordHash())){
            throw new IllegalArgumentException("Invalid credentials");
        }
        return jwtService.generateToken(user.getId(),user.getEmail(),user.getRole());
    }
}