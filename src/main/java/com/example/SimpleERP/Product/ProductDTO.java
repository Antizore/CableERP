package com.example.SimpleERP.Product;

import com.example.SimpleERP.BillOfMaterials.BillOfMaterialsDTO;

import java.util.List;

public record ProductDTO(Long id, String name, String description, List<BillOfMaterialsDTO> billOfMaterials) {

}
