package com.example.CableERP.Inventory;

public record CreateInventoryDTO(Long componentId, double qtyAvailable, double qtyReserved) {
}
