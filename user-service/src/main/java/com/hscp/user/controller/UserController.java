package com.hscp.user.controller;

import com.hscp.user.domain.User;
import com.hscp.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User create(@Valid @RequestBody CreateUserRequest request) {
        return userService.createUser(request.name(), request.email());
    }

    @GetMapping("/{id}")
    public User get(@PathVariable String id) {
        return userService.getUser(id);
    }

    public record CreateUserRequest(
            @NotBlank String name,
            @Email String email
    ) {}
}
