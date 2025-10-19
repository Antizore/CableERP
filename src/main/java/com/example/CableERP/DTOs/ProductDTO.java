package com.example.CableERP.DTOs;

import java.util.List;

public record ProductDTO(Long id, String name, String description, List<BillOfMaterialsDTO> billOfMaterials) {

}
