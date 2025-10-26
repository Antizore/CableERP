package com.example.CableERP.service;

import com.example.CableERP.entity.Component;
import com.example.CableERP.entity.Inventory;
import com.example.CableERP.exception.NoNameException;
import com.example.CableERP.exception.WrongValueException;
import com.example.CableERP.repository.ComponentRepository;
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
            // jeśli stworzy się nowy komponent to od razu zapiszemy go sobie w magazynie
            // tu masz błąd, bo ten komponent to ID jeszcze nie dostaje ogólnie, trochę kolejność nie ten tego
            // chyba będzie trza pośredni serwis uruchamiany przez kontroler komponenta przy post,
            // który wywoła komponent i inventory po kolei sobie
            Inventory inventory = new Inventory(null,0,0,component);
            inventoryService.createInventory(inventory);
            return componentRepository.saveAndFlush(component);
        }
    }


    public List<Component> getComponents(){
        return componentRepository.findAll();
    }


}
