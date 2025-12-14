package com.example.CableERP.Inventory;

import com.example.CableERP.Common.Exception.DuplicateException;
import com.example.CableERP.Common.Exception.MissingEntityException;
import com.example.CableERP.Common.Exception.WrongValueException;
import com.example.CableERP.Component.ComponentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {



    private final InventoryRepository inventoryRepository;
    private final ComponentRepository componentRepository;

    public InventoryService(InventoryRepository inventoryRepository, ComponentRepository componentRepository) {
        this.inventoryRepository = inventoryRepository;
        this.componentRepository = componentRepository;
    }


    public List<Inventory> returnInventoryList(){
        return inventoryRepository.findAll();
    }

    public Inventory returnSingleInventory(Long id){
        return inventoryRepository.findById(id).orElse(null);
    }


    public Inventory updateInventory(Long id, UpdateInventoryDTO updateInventoryDTO){
        Inventory currentInventory = inventoryRepository.findById(id).orElseThrow();
        if(updateInventoryDTO.qtyAvilable() < 0 || updateInventoryDTO.qtyReserved() < 0) throw new WrongValueException("qty value cannot be less than 0");
        currentInventory.setQtyReserved(updateInventoryDTO.qtyReserved());
        currentInventory.setQtyAvailable(updateInventoryDTO.qtyAvilable());
        return inventoryRepository.saveAndFlush(currentInventory);
    }


    // fixed some errors, but now it seems not elegant, should be overwritten
    public Inventory createInventory(CreateInventoryDTO inventory){

        if(inventory.componentId() == null){
            throw new WrongValueException("Component Id cannot be null");
        }
        else {
            Inventory inventory1 = inventoryRepository.findByComponentId(inventory.componentId());
            if( inventory1 != null)  throw new DuplicateException("Inventory for this component exists.");

            else {
                if(inventory.qtyAvilable() < 0 || inventory.qtyReserved() < 0) throw new WrongValueException("qty value cannot be less than 0");
                return inventoryRepository.saveAndFlush(
                        new Inventory(inventory.qtyAvilable(), inventory.qtyReserved(), componentRepository.findById(inventory.componentId()).orElseThrow())
                );
                 }
            }

    }

    public void deleteInventory(Long id){
        inventoryRepository.deleteById(id);
    }


}
