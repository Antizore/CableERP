package com.example.SimpleERP.Vendor;

public record ShowComponentVendorDTO(
        Long vendorId,
        String vendorName,
        double price,
        boolean isPreffered
) { }
