package com.example.CableERP.controller;

import com.example.CableERP.DTOs.UpdateInventoryDTO;
import com.example.CableERP.entity.Inventory;
import com.example.CableERP.repository.InventoryRepository;
import com.example.CableERP.service.InventoryService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }


    @GetMapping
    public ResponseEntity <List<Inventory>> getInventory(){

        return ResponseEntity.
                ok()
                .body(inventoryService.returnInventoryList());
    }

    @GetMapping("/{id}")
    public ResponseEntity <Inventory> getSingleInventory(@PathVariable Long id){

        return ResponseEntity.
                ok()
                .body(inventoryService.returnSingleInventory(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity <Inventory> updateSingleInventory(@PathVariable Long id, @RequestBody UpdateInventoryDTO updateInventoryDTO){
        Inventory inventory = inventoryService.updateInventory(id,updateInventoryDTO);
        return ResponseEntity
                .ok()
                .body(inventory);
    }


    @PostMapping
    public ResponseEntity<Inventory> addInventory(@RequestBody Inventory inventoryBody){
        Inventory inventory = inventoryService.createInventory(inventoryBody);
        URI location = URI.create("/inventory/"+inventory.getId());
        return ResponseEntity.
                created(location)
                .build();
    }





}

