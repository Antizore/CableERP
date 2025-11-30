package com.example.CableERP.MRP;


import com.example.CableERP.BillOfMaterials.BillOfMaterials;
import com.example.CableERP.BillOfMaterials.BillOfMaterialsRepository;
import com.example.CableERP.Component.ComponentMRPDTO;
import com.example.CableERP.Inventory.InventoryRepository;
import com.example.CableERP.WorkOrder.WorkOrder;
import com.example.CableERP.WorkOrder.WorkOrderRepository;
import com.example.CableERP.WorkOrder.WorkOrderStatus;
import java.util.*;


@org.springframework.stereotype.Service
public class Service {
    private final WorkOrderRepository workOrderRepository;
    private final InventoryRepository inventoryRepository;
    private final BillOfMaterialsRepository billOfMaterialsRepository;

    public Service(WorkOrderRepository workOrderRepository,InventoryRepository inventoryRepository, BillOfMaterialsRepository billOfMaterialsRepository){
        this.workOrderRepository = workOrderRepository;
        this.inventoryRepository = inventoryRepository;
        this.billOfMaterialsRepository = billOfMaterialsRepository;
    }

    public List<List<ComponentMRPDTO>> mrpRun(){
        List<WorkOrder> listOfPlannedWorkOrders = workOrderRepository.findByStatus(WorkOrderStatus.PLANNED);
        List<ComponentMRPDTO> grossComponentsRequired = calculateGrossRequirements(listOfPlannedWorkOrders);
        List<List<ComponentMRPDTO>> netComponentsRequirements = calculateNetRequirements(grossComponentsRequired);

        List<List<ComponentMRPDTO>> result = new ArrayList<>();
        result.add(grossComponentsRequired);
        result.add(netComponentsRequirements.get(0));
        result.add(netComponentsRequirements.get(1));




        return result;

    }

    private List<ComponentMRPDTO> calculateGrossRequirements(List<WorkOrder> listOfPlannedWorkOrders){
        List<ComponentMRPDTO> componentsNeeded = new ArrayList<>();

        for(WorkOrder workOrder : listOfPlannedWorkOrders){
            List<BillOfMaterials> billOfMaterialsList = workOrder.getProduct().getBillOfMaterialsList();
            for (BillOfMaterials bill : billOfMaterialsList){
                ComponentMRPDTO componentMRPDTO = new ComponentMRPDTO(bill.getComponent().getId(), bill.getComponent().getName(), bill.getQty());
                ComponentMRPDTO componentMRPDTO1 = componentsNeeded.stream().filter(a -> a == componentMRPDTO).findFirst().orElse(null);
                if(componentMRPDTO1 == null){
                    componentsNeeded.add(componentMRPDTO);
                }
                else {
                    componentMRPDTO1.setQty(componentMRPDTO1.getQty() + bill.getQty());
                }
            }
        }
        return componentsNeeded;
    }

    private List<List<ComponentMRPDTO>> calculateNetRequirements(List<ComponentMRPDTO> componentsRequired){

        List<ComponentMRPDTO> missingNetComponents = new ArrayList<>();
        List<ComponentMRPDTO> missingComponentsToBuy = new ArrayList<>();
        List<ComponentMRPDTO> missingComponentsToProduce = new ArrayList<>();
        List<List<ComponentMRPDTO>> result = new ArrayList<>();
        result.add(missingComponentsToBuy);
        result.add(missingComponentsToProduce);

        componentsRequired.forEach(
                (k) ->
                {
                    {
                        if(k.getQty() < inventoryRepository.findByComponentId(k.getId()).getQtyAvailable() + inventoryRepository.findByComponentId(k.getId()).getQtyReserved())
                        {
                            if(billOfMaterialsRepository.findAllByProduct_Name(k.getName()) == null || billOfMaterialsRepository.findAllByProduct_Name(k.getName()).isEmpty())
                            {
                                missingComponentsToBuy.add(k);
                                missingNetComponents.add(k);
                            }
                            else{
                                missingComponentsToProduce.add(k);
                                missingNetComponents.add(k);
                            }
                        }
                    }
                }
        );
        return result;
    }




}
