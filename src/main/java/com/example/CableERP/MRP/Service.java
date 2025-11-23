package com.example.CableERP.MRP;


import com.example.CableERP.BillOfMaterials.BillOfMaterials;
import com.example.CableERP.Component.Component;
import com.example.CableERP.Component.ComponentRepository;
import com.example.CableERP.Inventory.InventoryRepository;
import com.example.CableERP.WorkOrder.WorkOrder;
import com.example.CableERP.WorkOrder.WorkOrderRepository;
import com.example.CableERP.WorkOrder.WorkOrderService;
import com.example.CableERP.WorkOrder.WorkOrderStatus;

import java.util.HashMap;
import java.util.List;

@org.springframework.stereotype.Service
public class Service {


    private final WorkOrderRepository workOrderRepository;
    private final InventoryRepository inventoryRepository;

    public Service(WorkOrderRepository workOrderRepository,InventoryRepository inventoryRepository){
        this.workOrderRepository = workOrderRepository;
        this.inventoryRepository = inventoryRepository;

    }




    public void mrpRun(){
        List<WorkOrder> listOfPlannedWorkOrders = workOrderRepository.findByStatus(WorkOrderStatus.PLANNED);
        HashMap<Component, Double> componentsRequired = calculateGrossRequirements(listOfPlannedWorkOrders);
        System.out.println(calculateNetRequirements(componentsRequired));


    }


    //TODO liczymy zapotrzebowanie na wszystkie komponenty
    private HashMap<Component,Double> calculateGrossRequirements(List<WorkOrder> listOfPlannedWorkOrders){
        HashMap<Component, Double> componentsNeeded = new HashMap<>();
        for(WorkOrder workOrder : listOfPlannedWorkOrders){
            List<BillOfMaterials> billOfMaterialsList = workOrder.getProduct().getBillOfMaterialsList();
            for (BillOfMaterials bill : billOfMaterialsList){
                componentsNeeded.computeIfAbsent(bill.getComponent(), value -> bill.getQty() * workOrder.getQty());
                componentsNeeded.computeIfPresent(bill.getComponent(), (key,oldVal) -> (oldVal += bill.getQty() * workOrder.getQty()) );
            }
        }
        return componentsNeeded;
    }

    private HashMap<Component,Double> calculateNetRequirements(HashMap<Component, Double> componentsRequired){

        HashMap<Component, Double> missingComponents = new HashMap<>();

        componentsRequired.forEach(
                (k,v) ->
                {
                    if(v < inventoryRepository.findByComponentId(k.getId()).getQtyAvailable() + inventoryRepository.findByComponentId(k.getId()).getQtyReserved())
                    {
                        missingComponents.computeIfPresent(k, (k1,v1) -> (v1 += v));
                        missingComponents.computeIfAbsent(k, v1 -> v);
                    }
                }
        );
        return missingComponents;
    }



    //TODO
    private void planWorkOrders(){

    }

    //TODO
    private void planPurchaseOrders(){

    }


}
