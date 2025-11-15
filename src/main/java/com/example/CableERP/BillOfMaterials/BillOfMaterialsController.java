package com.example.CableERP.BillOfMaterials;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/{id}/bom")
public class BillOfMaterialsController {

    private final BillOfMaterialsService billOfMaterialsService;

    public BillOfMaterialsController(BillOfMaterialsService billOfMaterialsService) {
        this.billOfMaterialsService = billOfMaterialsService;
    }



    @GetMapping
    public ResponseEntity<List<BillOfMaterialsDTO>> getBill(@PathVariable Long id){
        return ResponseEntity
                .ok()
                .body(billOfMaterialsService.getBill(id));
    }



    @PostMapping
    public ResponseEntity<List<BillOfMaterials>> postBill(@RequestBody List<BomCreatingDTO> billOfMaterialsList, @PathVariable Long id){

        billOfMaterialsService.addBill(billOfMaterialsList,id);

        return ResponseEntity
                .ok()
                .build();
    }



}

