package com.example.SimpleERP.Inventory;

import com.example.SimpleERP.Component.ComponentResponseDTO;

public record ShowInventoryDTO(
        Long id,
        double qtyAvailable,
        double qtyReserved,
        ComponentResponseDTO component
) {
}
