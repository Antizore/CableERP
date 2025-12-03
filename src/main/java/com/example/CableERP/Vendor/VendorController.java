package com.example.CableERP.Vendor;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendors")
public class VendorController {

    private final VendorService vendorService;


    public VendorController(VendorService vendorService){
        this.vendorService = vendorService;
    }


    //TODO: DTO Z UPDATE
    @PostMapping
    public ResponseEntity<Vendor> addVendor(@RequestBody Vendor vendor){
        vendorService.addVendor(vendor);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Vendor>> getVendors(){
        return ResponseEntity
                .ok()
                .body(vendorService.getVendors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vendor> getVendor(@PathVariable Long id){
        return ResponseEntity
                .ok()
                .body(vendorService.getVendor(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vendor> updateVendor(@PathVariable Long id, @RequestBody UpdateVendorDTO vendor){
        vendorService.updateVendor(id, vendor);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteVendor(@PathVariable Long id){
        vendorService.deleteVendor(id);
        return ResponseEntity.ok().build();
    }

}
