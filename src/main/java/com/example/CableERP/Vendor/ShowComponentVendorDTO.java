package com.example.CableERP.Vendor;

public record ShowComponentVendorDTO(
        Long vendorId,
        String vendorName,
        double price,
        boolean isPreffered
) { }
