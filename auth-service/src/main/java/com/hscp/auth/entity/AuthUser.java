package com.hscp.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "auth_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String role; // USER / ADMIN
}