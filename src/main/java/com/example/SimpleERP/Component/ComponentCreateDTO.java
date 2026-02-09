package com.example.SimpleERP.Component;

import com.example.SimpleERP.Common.Unit;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ComponentCreateDTO(
        @NotNull(message = "Component name is required")
        @Size(min = 2, message = "Component name needs to have 2 or more characters")
        String name,
        @NotNull (message = "Component unit is required")
        Unit unit,
        @NotNull (message = "Cost per unit is required")
        @Digits(integer =10, fraction=2, message = "Currently supported (10,2)")
        @Positive(message = "The price must be greater than 0")
        Double costPerUnit) {
}
