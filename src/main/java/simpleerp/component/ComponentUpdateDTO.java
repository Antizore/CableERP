package simpleerp.component;

import simpleerp.common.Unit;

public record ComponentUpdateDTO(String name, Unit unit, Double costPerUnit) {
}
