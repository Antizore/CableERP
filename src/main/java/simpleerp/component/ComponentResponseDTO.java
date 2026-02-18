package simpleerp.component;


import simpleerp.common.Unit;
import simpleerp.vendor.ShowComponentVendorDTO;

import java.util.List;


public record ComponentResponseDTO(
        Long id,
        String name,
        Unit unit,
        Double costPerUnit,
        List<ShowComponentVendorDTO> componentVendorList
) {
}
