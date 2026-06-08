package simpleerp.component;

import simpleerp.common.Unit;

import java.math.BigDecimal;

public record ComponentUpdateDTO(String name, Unit unit, BigDecimal costPerUnit) {
}
