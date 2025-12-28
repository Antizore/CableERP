package com.example.CableERP.Component;

import com.example.CableERP.BillOfMaterials.BillOfMaterialsDTO;
import com.example.CableERP.BillOfMaterials.BillOfMaterialsService;
import com.example.CableERP.Common.Exception.CannotDeleteException;
import com.example.CableERP.Inventory.CreateInventoryDTO;
import com.example.CableERP.Common.Exception.NoNameException;
import com.example.CableERP.Common.Exception.WrongValueException;
import com.example.CableERP.Inventory.InventoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ComponentService {

    private final ComponentRepository componentRepository;
    private final InventoryService inventoryService;
    private final BillOfMaterialsService billOfMaterialsService;

    public ComponentService(ComponentRepository componentRepository, InventoryService inventoryService, BillOfMaterialsService billOfMaterialsService) {
        this.componentRepository = componentRepository;
        this.inventoryService = inventoryService;
        this.billOfMaterialsService = billOfMaterialsService;
    }


    public Component addComponent(ComponentCreateDTO component){
        if (component.name().isBlank()) throw new NoNameException("Component name cannot be blank");
        else if (component.costPerUnit() < 0) throw new WrongValueException("Cost of component cannot be less than 0");
        else
        {
            Component save = componentRepository.saveAndFlush(new Component(component.name(),component.unit(), component.costPerUnit()));
            CreateInventoryDTO inventory = new CreateInventoryDTO(save.getId(),0,0);
            inventoryService.createInventory(inventory);
            return save;
        }
    }


    public List<Component> getComponents(){
        return componentRepository.findAll();
    }

    public Component getComponent(Long id){return componentRepository.findById(id).orElseThrow();}



    public void deleteComponent(Long id){

        List<BillOfMaterialsDTO> dtoList = billOfMaterialsService.getBill(id, getComponent(id));

        if (dtoList == null || dtoList.isEmpty())
        {
            componentRepository.deleteById(getComponent(id).getId());
        }
        else {
            throw new CannotDeleteException("Cannot delete components that are actively used in BOM. Delete BOM first.");
        }


    }

    public Component patchComponent(Long id, ComponentCreateDTO component){

        Component updatedComponent = getComponent(id);

        if(!(component.name() == null || component.name().isBlank())) updatedComponent.setName(component.name());
        if(component.unit() != null) updatedComponent.setUnit(component.unit());
        if(!(component.costPerUnit() == null || component.costPerUnit().isNaN() || component.costPerUnit().isInfinite())) updatedComponent.setCostPerUnit(component.costPerUnit());

        componentRepository.saveAndFlush(updatedComponent);

        return updatedComponent;
    }



}
