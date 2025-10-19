package com.example.CableERP.controller;


import com.example.CableERP.DTOs.BillOfMaterialsDTO;
import com.example.CableERP.entity.BillOfMaterials;
import com.example.CableERP.service.BillOfMaterialsService;
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
    public ResponseEntity<List<BillOfMaterials>> postBill(@RequestBody List<BillOfMaterials> billOfMaterialsList){

        billOfMaterialsService.addBill(billOfMaterialsList);

        return ResponseEntity
                .ok()
                .build();
    }



}

