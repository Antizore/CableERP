package com.example.CableERP.Inventory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }


    @GetMapping
    public ResponseEntity<List<ShowInventoryDTO>> getInventory(){
        return ResponseEntity
                .ok()
                .body(inventoryService.getAllInventory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowInventoryDTO> getSingleInventory(@PathVariable Long id){
        return ResponseEntity
                .ok()
                .body(inventoryService.getInventoryById(id));
    }


    @PostMapping
    public ResponseEntity<Void> initializeInventory(@RequestBody CreateInventoryDTO inventoryBody){
        Inventory inventory = inventoryService.initializeOrUpdateInventory(inventoryBody);
        URI location = URI.create("/inventory/" + inventory.getId());
        return ResponseEntity
                .created(location)
                .build();
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Inventory> manualCorrection(@PathVariable Long id, @RequestBody UpdateInventoryDTO updateInventoryDTO){
        Inventory inventory = inventoryService.manualCorrection(id, updateInventoryDTO);
        return ResponseEntity
                .ok()
                .body(inventory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInventory(@PathVariable Long id){
        inventoryService.deleteInventory(id);
        return ResponseEntity
                .ok()
                .body("Deleted successfully");
    }

    @PostMapping("/receive")
    public ResponseEntity<String> receiveGoods(@RequestBody InventoryTransactionDTO transaction) {
        inventoryService.receiveGoods(transaction.componentId(), transaction.qty());
        return ResponseEntity.ok("Goods received successfully");
    }

    @PostMapping("/issue")
    public ResponseEntity<String> issueGoods(@RequestBody InventoryTransactionDTO transaction) {
        inventoryService.issueGoods(transaction.componentId(), transaction.qty());
        return ResponseEntity.ok("Goods issued to production successfully");
    }
}