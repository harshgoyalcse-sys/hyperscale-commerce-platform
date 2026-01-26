package com.hscp.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class User {

    private String id;
    private String name;
    private String email;
    private Instant createdAt;
}
