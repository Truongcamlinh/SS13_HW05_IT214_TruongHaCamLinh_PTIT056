package com.storex.inventory.model;

public record InventoryDto(
        Long productId,
        int requestedQuantity,
        int availableQuantity,
        boolean available,
        String message) {
}

