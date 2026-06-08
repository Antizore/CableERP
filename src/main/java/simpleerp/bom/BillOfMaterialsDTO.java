package simpleerp.bom;


import simpleerp.component.ComponentDTO;

import java.math.BigDecimal;

public record BillOfMaterialsDTO(BigDecimal qty, ComponentDTO component) {}
