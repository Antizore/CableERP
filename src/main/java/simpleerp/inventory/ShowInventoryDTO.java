package simpleerp.inventory;

import simpleerp.component.ComponentResponseDTO;

import java.math.BigDecimal;

public record ShowInventoryDTO(
        Long id,
        BigDecimal qtyAvailable,
        BigDecimal qtyReserved,
        ComponentResponseDTO component
) {
}
