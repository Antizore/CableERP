package com.example.CableERP.Component;


import com.example.CableERP.Common.Unit;
import com.example.CableERP.Vendor.ShowComponentVendorDTO;

import java.util.List;


public record ComponentResponseDTO(
        Long id,
        String name,
        Unit unit,
        Double costPerUnit,
        List<ShowComponentVendorDTO> componentVendorList
) {
}
