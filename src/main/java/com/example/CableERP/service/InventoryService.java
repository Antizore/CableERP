package com.example.CableERP.service;

import com.example.CableERP.entity.Inventory;
import com.example.CableERP.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {



    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }


    public List<Inventory> returnInventoryList(){
        return inventoryRepository.findAll();
    }

    /*
        TODO:
         🧩 Logika:
         jeśli inventory_item istnieje → zwiększ qty_available
         jeśli nie → utwórz nowy rekord
     */
    public Inventory createInventory(Inventory inventory){
        if(inventoryRepository.existsById(inventory.getId())){
            Inventory inventoryToUpdate = inventoryRepository.findById(inventory.getId()).get();
            inventoryToUpdate.setQtyAvailable(inventoryToUpdate.getQtyAvailable() + inventory.getQtyAvailable());
            return inventoryRepository.saveAndFlush(inventoryToUpdate);
        }
        else {
            return inventoryRepository.saveAndFlush(inventory);
        }


    }


}
