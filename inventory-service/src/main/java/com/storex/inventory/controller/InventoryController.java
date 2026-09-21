package com.storex.inventory.controller;

import com.storex.inventory.model.InventoryDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    @GetMapping("/check")
    public Mono<InventoryDto> check(
            @RequestParam Long productId,
            @RequestParam int quantity) {
        int stock = productId % 2 == 0 ? 20 : 3;
        boolean available = stock >= quantity;
        return Mono.just(new InventoryDto(
                productId,
                quantity,
                stock,
                available,
                available ? "Đủ hàng" : "Không đủ hàng"));
    }
}

