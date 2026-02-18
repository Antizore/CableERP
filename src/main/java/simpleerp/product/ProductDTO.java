package simpleerp.product;

import simpleerp.bom.BillOfMaterialsDTO;

import java.util.List;

public record ProductDTO(Long id, String name, String description, List<BillOfMaterialsDTO> billOfMaterials) {

}
