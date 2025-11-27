package com.example.CableERP.MRP;


import com.example.CableERP.BillOfMaterials.BillOfMaterials;
import com.example.CableERP.BillOfMaterials.BillOfMaterialsRepository;
import com.example.CableERP.Component.Component;
import com.example.CableERP.Component.ComponentMRPDTO;
import com.example.CableERP.Inventory.InventoryRepository;
import com.example.CableERP.WorkOrder.WorkOrder;
import com.example.CableERP.WorkOrder.WorkOrderRepository;
import com.example.CableERP.WorkOrder.WorkOrderStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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




    public List<ComponentMRPDTO> mrpRun(){
        List<WorkOrder> listOfPlannedWorkOrders = workOrderRepository.findByStatus(WorkOrderStatus.PLANNED);
        List<ComponentMRPDTO> grossComponentsRequired = calculateGrossRequirements(listOfPlannedWorkOrders);
        //List<HashMap<ComponentMRPDTO,Double>> netComponentsRequirements = calculateNetRequirements(GrossComponentsRequired);

        /*
        HashMap<String,HashMap<ComponentMRPDTO,Double>> summary = new HashMap<>();

        summary.put("Missing components to produce: ",netComponentsRequirements.get(0));
        summary.put("Missing components to buy: ", netComponentsRequirements.get(1));
         */



        return grossComponentsRequired;

    }


    private List<ComponentMRPDTO> calculateGrossRequirements(List<WorkOrder> listOfPlannedWorkOrders){
        //HashMap<ComponentMRPDTO, Double> componentsNeeded = new HashMap<>();
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





                /*
                componentsNeeded.computeIfAbsent(
                        new ComponentMRPDTO(bill.getComponent().getId(), bill.getComponent().getName()),
                        value -> bill.getQty() * workOrder.getQty());
                componentsNeeded.computeIfPresent(new ComponentMRPDTO(bill.getComponent().getId(), bill.getComponent().getName()),
                        (key,oldVal) -> (oldVal += bill.getQty() * workOrder.getQty()) );

                 */
            }
        }
        return componentsNeeded;
    }
/*
    private List<HashMap<ComponentMRPDTO,Double>> calculateNetRequirements(HashMap<ComponentMRPDTO, Double> componentsRequired){

        //List<ComponentMRPDTO> missingComponentsToProduce = new ArrayList<>();
        HashMap<ComponentMRPDTO, Double> missingComponentsToProduce = new HashMap<>();
        //HashMap<ComponentMRPDTO, Double> missingComponentsToBuy = new HashMap<>();
        List<ComponentMRPDTO> missingComponentsToBuy = new ArrayList<>();
        //List<HashMap<ComponentMRPDTO,Double>> result = List.of(missingComponentsToBuy,missingComponentsToProduce);


        componentsRequired.forEach(
                (k,v) ->
                {
                    {
                        if(v < inventoryRepository.findByComponentId(k.id()).getQtyAvailable() + inventoryRepository.findByComponentId(k.id()).getQtyReserved())
                        {
                            if(billOfMaterialsRepository.findAllByProduct_Name(k.name()) == null || billOfMaterialsRepository.findAllByProduct_Name(k.name()).isEmpty())
                            {

                                //missingComponentsToBuy.computeIfPresent(k, (k1,v1) -> (v1 += v));
                                //missingComponentsToBuy.computeIfAbsent(k, v1 -> v);
                            }
                            else{
                                missingComponentsToProduce.computeIfPresent(k, (k1,v1) -> (v1 += v));
                                missingComponentsToProduce.computeIfAbsent(k, v1 -> v);
                            }
                        }
                    }
                }
        );
        return result;
    }



 */
}
