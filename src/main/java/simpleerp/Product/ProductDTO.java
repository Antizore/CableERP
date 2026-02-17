package simpleerp.Product;

import simpleerp.BillOfMaterials.BillOfMaterialsDTO;

import java.util.List;

public record ProductDTO(Long id, String name, String description, List<BillOfMaterialsDTO> billOfMaterials) {

}
