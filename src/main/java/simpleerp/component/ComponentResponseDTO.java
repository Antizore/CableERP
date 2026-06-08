package simpleerp.component;


import simpleerp.common.Unit;
import simpleerp.vendor.ShowComponentVendorDTO;

import java.math.BigDecimal;
import java.util.List;


public record ComponentResponseDTO(
        Long id,
        String name,
        Unit unit,
        BigDecimal costPerUnit,
        List<ShowComponentVendorDTO> componentVendorList
) {
}
