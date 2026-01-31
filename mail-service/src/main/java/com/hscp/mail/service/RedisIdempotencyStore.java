package com.hscp.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisIdempotencyStore implements IdempotencyStore {

    private final StringRedisTemplate redisTemplate;

    private static final Duration TTL = Duration.ofDays(7);

    @Override
    public boolean isProcessed(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public void markProcessed(String key) {
        redisTemplate.opsForValue().set(key, "1", TTL);
    }
}