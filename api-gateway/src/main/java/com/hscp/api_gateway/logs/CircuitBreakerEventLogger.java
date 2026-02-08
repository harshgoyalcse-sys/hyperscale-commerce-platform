package com.hscp.api_gateway.logs;

import com.hscp.api_gateway.fallback.FallbackController;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CircuitBreakerEventLogger {
    private static final Logger log =
            LoggerFactory.getLogger(CircuitBreakerEventLogger.class);

    public CircuitBreakerEventLogger(CircuitBreakerRegistry registry) {

        registry.getAllCircuitBreakers().forEach(cb -> {

            cb.getEventPublisher()
                    .onStateTransition(event ->
                            log.warn("🔄 CIRCUIT STATE CHANGE [{}]: {} → {}",
                                    cb.getName(),
                                    event.getStateTransition().getFromState(),
                                    event.getStateTransition().getToState()
                            )
                    )
                    .onFailureRateExceeded(event ->
                            log.error("❌ FAILURE RATE EXCEEDED [{}]: {}%",
                                    cb.getName(),
                                    event.getFailureRate()
                            )
                    )
                    .onCallNotPermitted(event ->
                            log.warn("⛔ CALL BLOCKED [{}] – circuit OPEN",
                                    cb.getName()
                            )
                    );
        });
    }
}
