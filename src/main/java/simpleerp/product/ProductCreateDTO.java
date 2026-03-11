package simpleerp.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

//TODO: add minutesToProduce with corresponding constraints
public record ProductCreateDTO(
        @NotNull
        @Size(min = 2, message = "product name needs to have 2 or more characters")
        String name,
        String description
) { }
