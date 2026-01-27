package com.example.CableERP.Inventory;

import com.example.CableERP.Component.ComponentResponseDTO;

public record ShowInventoryDTO(
        Long id,
        double qtyAvailable,
        double qtyReserved,
        ComponentResponseDTO component
) {
}
