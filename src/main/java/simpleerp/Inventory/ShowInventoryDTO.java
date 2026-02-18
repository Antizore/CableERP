package simpleerp.Inventory;

import simpleerp.component.ComponentResponseDTO;

public record ShowInventoryDTO(
        Long id,
        double qtyAvailable,
        double qtyReserved,
        ComponentResponseDTO component
) {
}
