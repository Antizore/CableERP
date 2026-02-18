package simpleerp.component;

import simpleerp.Common.Unit;

public record ComponentUpdateDTO(String name, Unit unit, Double costPerUnit) {
}
