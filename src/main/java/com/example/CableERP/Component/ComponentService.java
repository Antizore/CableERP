package com.example.CableERP.Component;

import com.example.CableERP.Inventory.Inventory;
import com.example.CableERP.exception.NoNameException;
import com.example.CableERP.exception.WrongValueException;
import com.example.CableERP.Inventory.InventoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ComponentService {

    private final ComponentRepository componentRepository;
    private final InventoryService inventoryService;

    public ComponentService(ComponentRepository componentRepository, InventoryService inventoryService) {
        this.componentRepository = componentRepository;
        this.inventoryService = inventoryService;
    }


    public Component addComponent(Component component) throws Exception{
        if (component.getName().isBlank()) throw new NoNameException("Component name cannot be blank");
        else if (component.getCostPerUnit() < 0) throw new WrongValueException("Cost of component cannot be less than 0");
        else
        {
            var save = componentRepository.saveAndFlush(component);
            Inventory inventory = new Inventory(0,0,save);
            inventoryService.createInventory(inventory);
            return save;
        }
    }


    public List<Component> getComponents(){
        return componentRepository.findAll();
    }


}
