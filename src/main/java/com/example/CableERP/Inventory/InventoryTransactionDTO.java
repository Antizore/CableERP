package com.example.CableERP.Inventory;

public record InventoryTransactionDTO(
        Long componentId,
        double qty
) {}