package com.storex.order.controller;

import com.storex.order.client.InventoryClient;
import com.storex.order.model.InventoryDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/orders")
public class OrderInventoryController {
    private final InventoryClient inventoryClient;

    public OrderInventoryController(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    @GetMapping("/inventory")
    public Mono<InventoryDto> checkInventory(
            @RequestParam Long productId,
            @RequestParam int quantity) {
        return inventoryClient.check(productId, quantity);
    }
}

