package simpleerp.component;

import simpleerp.common.Unit;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ComponentCreateDTO(
        @NotNull(message = "component name is required")
        @Size(min = 2, message = "component name needs to have 2 or more characters")
        String name,
        @NotNull (message = "component unit is required")
        Unit unit,
        @NotNull (message = "Cost per unit is required")
        @Digits(integer =10, fraction=2, message = "Currently supported (10,2)")
        @Positive(message = "The price must be greater than 0")
        BigDecimal costPerUnit) {
}
