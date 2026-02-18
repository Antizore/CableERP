package simpleerp.inventory;

public record CreateInventoryDTO(Long componentId, double qtyAvailable, double qtyReserved) {
}
