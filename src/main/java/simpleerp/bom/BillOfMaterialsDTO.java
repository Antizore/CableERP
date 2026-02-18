package simpleerp.bom;


import simpleerp.component.ComponentDTO;

public record BillOfMaterialsDTO(double qty, ComponentDTO component) {}
