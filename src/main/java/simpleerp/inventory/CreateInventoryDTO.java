package simpleerp.inventory;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateInventoryDTO(

        @NotNull(message = "Component id is required")
        Long componentId,

        @NotNull
        @Digits(integer =10, fraction=2, message = "Currently supported (10,2)")
        @Min(value = 0, message = "qty cannot be less than 0")
        double qtyAvailable,

        @NotNull
        @Digits(integer =10, fraction=2, message = "Currently supported (10,2)")
        @Min(value = 0, message = "qty cannot be less than 0")
        double qtyReserved
) { }
