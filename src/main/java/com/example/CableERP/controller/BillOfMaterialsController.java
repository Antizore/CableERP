package com.example.CableERP.controller;


import com.example.CableERP.entity.BillOfMaterials;
import com.example.CableERP.service.BillOfMaterialsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/{id}/bom")
public class BillOfMaterialsController {

    private BillOfMaterialsService billOfMaterialsService;

    //  TODO: PRZEROBIĆ, POTRZEBNE JEST DTO
    @GetMapping
    public ResponseEntity<List<BillOfMaterials>> getBill(@PathVariable Long id){
        return ResponseEntity
                .ok()
                .body(billOfMaterialsService.getBill(id));
    }





}
