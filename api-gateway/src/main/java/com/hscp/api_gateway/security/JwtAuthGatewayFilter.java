package com.hscp.api_gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class JwtAuthGatewayFilter {

    private static final Logger log =
            LoggerFactory.getLogger(JwtAuthGatewayFilter.class);
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public GlobalFilter jwtFilter() {
        return (exchange, chain) -> {

            String path = exchange.getRequest().getURI().getPath();
            String method = exchange.getRequest().getMethod().name();

            log.debug("➡️ Incoming request: {} {}", method, path);

            // ✅ Public endpoints
            if (path.startsWith("/auth") || path.startsWith("/actuator")) {
                log.debug("🟢 Public endpoint, skipping JWT check: {}", path);
                return chain.filter(exchange);
            }

            String authHeader =
                    exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null) {
                log.warn("❌ Missing Authorization header for path: {}", path);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            if (!authHeader.startsWith("Bearer ")) {
                log.warn("❌ Invalid Authorization header format for path: {}", path);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);

            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
                        .parseClaimsJws(token)
                        .getBody();

                String userId = claims.getSubject();
                String role = claims.get("role", String.class);

                if (role == null || role.isBlank()) {
                    log.warn("❌ Role claim missing in JWT");
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }

                List<String> roles = List.of(role);

                log.debug("🎭 roles: {}", roles);
                log.debug("✅ JWT validated successfully");
                log.debug("👤 userId: {}", userId);
                log.debug("🎭 roles: {}", roles);

                ServerWebExchange mutated = exchange.mutate()
                        .request(r -> r
                                .header("X-User-Id", userId)
                                .header("X-Roles", String.join(",", roles))
                        )
                        .build();

                return chain.filter(mutated);

            } catch (Exception e) {
                log.warn("❌ JWT validation failed for path {} : {}", path, e.getMessage());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };
    }
}
