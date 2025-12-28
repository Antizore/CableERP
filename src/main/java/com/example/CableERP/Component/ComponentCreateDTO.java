package com.example.CableERP.Component;

import com.example.CableERP.Common.Unit;

public record ComponentCreateDTO(String name, Unit unit, Double costPerUnit) {
}
