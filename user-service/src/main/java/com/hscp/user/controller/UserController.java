package com.hscp.user.controller;

import com.hscp.user.domain.User;
import com.hscp.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Gateway already authenticated & authorized
    @GetMapping("/me")
    public String me(Authentication authentication) {
        return authentication.getName(); // userId
    }

    @GetMapping("/slow")
    public String slow() throws InterruptedException {
        Thread.sleep(5000); // 5 seconds
        return "slow";
    }


    // Public (allowed by gateway)
    @GetMapping("/hello")
    public String hello() {
        return "Hello from User Service";
    }

    // ADMIN enforced at gateway
    @GetMapping("/admin/users/{id}")
    public User adminGetUser(@PathVariable String id) {
        return userService.getUser(id);
    }

    // Gateway decides who can create
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
