package com.example.CableERP.Component;

import com.example.CableERP.Common.Unit;
import jakarta.validation.constraints.NotNull;

public record ComponentCreateDTO(
        @NotNull String name,
        @NotNull Unit unit,
        @NotNull Double costPerUnit) {
}
