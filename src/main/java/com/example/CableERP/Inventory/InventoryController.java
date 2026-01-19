package com.example.CableERP.Inventory;

import org.springframework.data.jpa.repository.Query;
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


    @GetMapping()
    public ResponseEntity<?> getInventory(){
            return ResponseEntity.
                    ok()
                    .body(inventoryService.returnInventoryList());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity <Inventory> getSingleInventory(@PathVariable Long id){
            return ResponseEntity.
                    ok()
                    .body(inventoryService.returnSingleInventory(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity <Inventory> updateSingleInventory(@PathVariable Long id, @RequestBody UpdateInventoryDTO updateInventoryDTO){
        Inventory inventory = inventoryService.updateInventory(id,updateInventoryDTO);
        return ResponseEntity
                .ok()
                .body(inventory);
    }


    @PostMapping
    public ResponseEntity<Inventory> addInventory(@RequestBody CreateInventoryDTO inventoryBody){
        Inventory inventory = inventoryService.createInventory(inventoryBody);
        URI location = URI.create("/inventory/"+inventory.getId());
        return ResponseEntity.
                created(location)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSingleInventory(@PathVariable Long id){
        inventoryService.deleteInventory(id);
        return ResponseEntity
                .ok()
                .body("Deleted successfully");
    }



}

