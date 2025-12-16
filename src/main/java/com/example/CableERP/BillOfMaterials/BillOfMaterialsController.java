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



    // there is no use of making get all BOMs because you can achieve it by just getting all the products, because
    // in product you have associated BOM to it
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


    // TODO: sprawdź czy nie lepiej PUT
    @PatchMapping
    public ResponseEntity<List<BillOfMaterials>> updateBill(@RequestBody List<BomCreatingDTO> billOfMaterialsList, @PathVariable Long id){
        billOfMaterialsService.updateBill(billOfMaterialsList, id);
        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping
    public ResponseEntity deleteBill(@PathVariable Long id){
        billOfMaterialsService.deleteBill(id);
        return ResponseEntity
                .ok()
                .build();
    }





}

