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
import java.util.Optional;

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
            return componentRepository.saveAndFlush(new Component(component.name(),component.unit(), component.costPerUnit()));
        }
    }


    public List<ComponentResponseDTO> getComponents(){
        return componentRepository.findAll().stream().map(
                component -> new ComponentResponseDTO(
                        component.getId(),
                        component.getName(),
                        component.getUnit(),
                        component.getCostPerUnit()
                )
        ).toList();
    }

    public ComponentResponseDTO getComponent(Long id){
        Component component = componentRepository.findById(id).orElseThrow();

        return new ComponentResponseDTO(
                 component.getId(),
                 component.getName(),
                 component.getUnit(),
                 component.getCostPerUnit()
         );
    }

    public Component getComponentService(Long id){
        return componentRepository.findById(id).orElseThrow();
    }



    public void deleteComponent(Long id){

        List<BillOfMaterialsDTO> dtoList = billOfMaterialsService.getBill(id, getComponentService(id));

        if (dtoList == null || dtoList.isEmpty())
        {
            componentRepository.deleteById(getComponentService(id).getId());
        }
        else {
            throw new CannotDeleteException("Cannot delete components that are actively used in BOM. Delete BOM first.");
        }


    }

    public Component patchComponent(Long id, ComponentUpdateDTO dto){
        Component component = getComponentService(id);

        Optional.ofNullable(dto.name())
                .filter(name -> !name.isBlank())
                .ifPresent(component::setName);

        Optional.ofNullable(dto.unit())
                .ifPresent(component::setUnit);

        Optional.ofNullable(dto.costPerUnit())
                .filter(costPerUnit -> !Double.isNaN(costPerUnit) && !Double.isInfinite(costPerUnit))
                .ifPresent(component::setCostPerUnit);

        return componentRepository.saveAndFlush(component);
    }



}
