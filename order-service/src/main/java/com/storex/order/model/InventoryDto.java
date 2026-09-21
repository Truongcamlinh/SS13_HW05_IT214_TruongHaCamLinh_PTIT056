package com.storex.order.model;

public record InventoryDto(
        Long productId,
        int requestedQuantity,
        int availableQuantity,
        boolean available,
        String message) {

    public static InventoryDto fallback(Long productId, int quantity) {
        return new InventoryDto(
                productId, quantity, 0, false,
                "Không thể kiểm tra tồn kho, vui lòng thử lại sau");
    }
}

