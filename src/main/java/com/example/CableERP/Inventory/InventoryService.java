package com.example.CableERP.Inventory;

import com.example.CableERP.Common.Exception.WrongValueException;
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

    public Inventory returnSingleInventory(Long id){
        return inventoryRepository.findById(id).orElse(null);
    }


    public Inventory updateInventory(Long id, UpdateInventoryDTO updateInventoryDTO){
        Inventory currentInventory = inventoryRepository.findById(id).get();
        if(updateInventoryDTO.qtyAvilable() < 0 || updateInventoryDTO.qtyReserved() < 0) throw new WrongValueException("qty value cannot be less than 0");
        currentInventory.setQtyReserved(updateInventoryDTO.qtyReserved());
        currentInventory.setQtyAvailable(updateInventoryDTO.qtyAvilable());
        return inventoryRepository.saveAndFlush(currentInventory);
    }


    public Inventory createInventory(Inventory inventory){
        if(inventory.getId() == null){
            return inventoryRepository.saveAndFlush(inventory);
        }
        else {
            if(inventory.getQtyAvailable() < 0 || inventory.getQtyReserved() < 0) throw new WrongValueException("qty value cannot be less than 0");
            Inventory inventoryToUpdate = inventoryRepository.findById(inventory.getId()).get();
            inventoryToUpdate.setQtyAvailable(inventoryToUpdate.getQtyAvailable() + inventory.getQtyAvailable());
            return inventoryRepository.saveAndFlush(inventoryToUpdate);
        }


    }

    public void deleteInventory(Long id){
        inventoryRepository.deleteById(id);
    }


}
