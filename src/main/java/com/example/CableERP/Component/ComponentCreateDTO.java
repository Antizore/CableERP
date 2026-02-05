package com.example.CableERP.Component;

import com.example.CableERP.Common.Unit;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ComponentCreateDTO(
        @NotNull @Size(min = 2) String name,
        @NotNull Unit unit,
        @NotNull Double costPerUnit) {
}
