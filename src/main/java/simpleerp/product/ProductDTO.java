package simpleerp.product;

import simpleerp.bom.BillOfMaterialsDTO;

import java.util.List;

// Used only for Get and Patch
// TODO: should be only used for get. Patch should be another dto with constraints
public record ProductDTO(
        Long id,
        String name,
        String description,
        List<BillOfMaterialsDTO> billOfMaterials) {

}
