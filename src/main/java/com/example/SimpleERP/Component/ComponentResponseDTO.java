package com.example.SimpleERP.Component;


import com.example.SimpleERP.Common.Unit;
import com.example.SimpleERP.Vendor.ShowComponentVendorDTO;

import java.util.List;


public record ComponentResponseDTO(
        Long id,
        String name,
        Unit unit,
        Double costPerUnit,
        List<ShowComponentVendorDTO> componentVendorList
) {
}
