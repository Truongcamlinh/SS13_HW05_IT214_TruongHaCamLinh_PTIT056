package com.storex.order.client;

import com.storex.order.model.InventoryDto;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class InventoryClient {
    private static final Logger log = LoggerFactory.getLogger(InventoryClient.class);

    private final WebClient webClient;
    private final CircuitBreaker circuitBreaker;

    public InventoryClient(
            WebClient inventoryWebClient,
            CircuitBreakerRegistry circuitBreakerRegistry) {
        this.webClient = inventoryWebClient;
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("inventoryClient");
    }

    public Mono<InventoryDto> check(Long productId, int quantity) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/inventory/check")
                        .queryParam("productId", productId)
                        .queryParam("quantity", quantity)
                        .build())
                .retrieve()
                .bodyToMono(InventoryDto.class)
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                .onErrorResume(error -> {
                    log.warn("Kiểm tra tồn kho thất bại: {}", error.getMessage());
                    return Mono.just(InventoryDto.fallback(productId, quantity));
                });
    }
}

