package com.hscp.api_gateway.ratelimit;

import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisLimiterConfig {

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        // replenishRate = tokens per second
        // burstCapacity = max tokens allowed at once
        return new RedisRateLimiter(10, 20);
    }
}
