package simpleerp.Inventory;

public record InventoryTransactionDTO(
        Long componentId,
        double qty
) {}