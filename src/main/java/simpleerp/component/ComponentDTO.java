package simpleerp.component;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ComponentDTO(
        @NotNull(message = "component name is required")
        @Size(min = 2, message = "component name needs to have 2 or more characters")
        String name,

        @NotNull (message = "component unit is required")
        String unit
) {}