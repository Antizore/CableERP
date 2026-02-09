package com.example.SimpleERP.Inventory;

public record CreateInventoryDTO(Long componentId, double qtyAvailable, double qtyReserved) {
}
