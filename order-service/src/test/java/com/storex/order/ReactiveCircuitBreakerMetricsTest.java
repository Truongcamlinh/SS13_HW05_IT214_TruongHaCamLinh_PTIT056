package com.storex.order;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReactiveCircuitBreakerMetricsTest {
    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    private MeterRegistry meterRegistry;

    @Test
    void exposesClosedStateMetricForInventoryClient() {
        var circuitBreaker = circuitBreakerRegistry.circuitBreaker("inventoryClient");

        assertThat(circuitBreaker.getState().name()).isEqualTo("CLOSED");
        assertThat(meterRegistry.find("resilience4j.circuitbreaker.state")
                .tags("name", "inventoryClient", "state", "closed")
                .gauge()).isNotNull();
    }
}

