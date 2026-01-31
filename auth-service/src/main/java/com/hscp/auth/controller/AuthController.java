package com.hscp.auth.controller;

import com.hscp.auth.dto.AuthResponse;
import com.hscp.auth.dto.LoginRequest;
import com.hscp.auth.dto.RegisterRequest;
import com.hscp.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid RegisterRequest request) {
        authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest request){
        String token = authService.login(request);
        return new AuthResponse(token);
    }
}