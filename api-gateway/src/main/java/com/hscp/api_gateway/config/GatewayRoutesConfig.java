package com.hscp.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hscp.api_gateway.ratelimit.RateLimitConfig;
import com.hscp.api_gateway.ratelimit.RedisLimiterConfig;

@Configuration
public class GatewayRoutesConfig {

    private final RateLimitConfig rateLimitConfig;
    private final RedisLimiterConfig redisLimiterConfig;

    public GatewayRoutesConfig(
            RateLimitConfig rateLimitConfig,
            RedisLimiterConfig redisLimiterConfig) {
        this.rateLimitConfig = rateLimitConfig;
        this.redisLimiterConfig = redisLimiterConfig;
    }

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {

        return builder.routes()

                // USER SERVICE
                .route("user-service", r -> r
                        .path("/users/**")
                        .filters(f -> f
                                .requestRateLimiter(c -> c
                                        .setRateLimiter(redisLimiterConfig.redisRateLimiter())
                                        .setKeyResolver(rateLimitConfig.userKeyResolver())
                                )
                                .circuitBreaker(cb -> cb
                                        .setName("userServiceCB")
                                        .setFallbackUri("forward:/fallback/user-service")
                                )
                        )
                        .uri("lb://USER-SERVICE")
                )

                .build();
    }
}
