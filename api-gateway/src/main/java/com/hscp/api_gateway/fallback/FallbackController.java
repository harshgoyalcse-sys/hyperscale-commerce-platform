package com.hscp.api_gateway.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
    private static final Logger log =
            LoggerFactory.getLogger(FallbackController.class);

    @GetMapping("/fallback/user-service")
    public ResponseEntity<String> userServiceFallback(ServerHttpRequest request) {

        log.warn("🚨 CIRCUIT OPEN - User Service fallback triggered");
        log.warn("🔁 Request path: {}", request.getPath());

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("User service is temporarily unavailable. Please try again later.");
    }
}
