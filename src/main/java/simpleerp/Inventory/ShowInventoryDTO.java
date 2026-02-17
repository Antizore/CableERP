package simpleerp.Inventory;

import simpleerp.Component.ComponentResponseDTO;

public record ShowInventoryDTO(
        Long id,
        double qtyAvailable,
        double qtyReserved,
        ComponentResponseDTO component
) {
}
