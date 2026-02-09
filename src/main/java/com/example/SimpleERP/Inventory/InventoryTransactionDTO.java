package com.example.SimpleERP.Inventory;

public record InventoryTransactionDTO(
        Long componentId,
        double qty
) {}