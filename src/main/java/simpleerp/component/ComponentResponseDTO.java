package simpleerp.component;


import simpleerp.Common.Unit;
import simpleerp.Vendor.ShowComponentVendorDTO;

import java.util.List;


public record ComponentResponseDTO(
        Long id,
        String name,
        Unit unit,
        Double costPerUnit,
        List<ShowComponentVendorDTO> componentVendorList
) {
}
