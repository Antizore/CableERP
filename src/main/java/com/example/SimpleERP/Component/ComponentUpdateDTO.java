package com.example.SimpleERP.Component;

import com.example.SimpleERP.Common.Unit;

public record ComponentUpdateDTO(String name, Unit unit, Double costPerUnit) {
}
