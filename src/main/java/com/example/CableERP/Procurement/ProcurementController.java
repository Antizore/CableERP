package com.example.CableERP.Procurement;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendors")
public class ProcurementController {

    private final ProcurementService procurementService;


    public ProcurementController(ProcurementService procurementService){
        this.procurementService = procurementService;
    }


    //TODO: DTO Z UPDATE
    @PostMapping
    public ResponseEntity<Procurement> addVendor(@RequestBody Procurement procurement){
        procurementService.addVendor(procurement);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Procurement>> getVendors(){
        return ResponseEntity
                .ok()
                .body(procurementService.getVendors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Procurement> getVendor(@PathVariable Long id){
        return ResponseEntity
                .ok()
                .body(procurementService.getVendor(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Procurement> updateVendor(@PathVariable Long id, @RequestBody UpdateProcurementDTO vendor){
        procurementService.updateVendor(id, vendor);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteVendor(@PathVariable Long id){
        procurementService.deleteVendor(id);
        return ResponseEntity.ok().build();
    }

}
