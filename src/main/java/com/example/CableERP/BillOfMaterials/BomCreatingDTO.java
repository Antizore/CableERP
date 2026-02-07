package com.example.CableERP.BillOfMaterials;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BomCreatingDTO(
        @Positive(message = "ID must be greater than 0")
        @NotNull(message = "ID cannot be null")
        @Digits(integer = 19,fraction = 0, message = "ID cannot have more than 19 digits")
        Long componentId,
        @Positive(message = "qty must be greater than 0")
        @NotNull(message = "qty cannot be null")
        @Digits(integer = 10, fraction = 4, message = "Currently supported (10,4)")
        double qty){}
