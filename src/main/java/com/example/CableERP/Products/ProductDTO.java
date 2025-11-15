package com.example.CableERP.Products;

import com.example.CableERP.BillOfMaterials.BillOfMaterialsDTO;

import java.util.List;

public record ProductDTO(Long id, String name, String description, List<BillOfMaterialsDTO> billOfMaterials) {

}
