package simpleerp.vendor;

public record ShowComponentVendorDTO(
        Long vendorId,
        String vendorName,
        double price,
        boolean isPreffered
) { }
