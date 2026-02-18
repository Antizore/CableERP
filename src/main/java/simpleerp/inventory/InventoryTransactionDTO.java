package simpleerp.inventory;

public record InventoryTransactionDTO(
        Long componentId,
        double qty
) {}