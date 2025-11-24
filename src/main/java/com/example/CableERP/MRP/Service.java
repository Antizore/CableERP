package com.example.CableERP.MRP;


import com.example.CableERP.BillOfMaterials.BillOfMaterials;
import com.example.CableERP.BillOfMaterials.BillOfMaterialsRepository;
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
    private final BillOfMaterialsRepository billOfMaterialsRepository;

    public Service(WorkOrderRepository workOrderRepository,InventoryRepository inventoryRepository, BillOfMaterialsRepository billOfMaterialsRepository){
        this.workOrderRepository = workOrderRepository;
        this.inventoryRepository = inventoryRepository;
        this.billOfMaterialsRepository = billOfMaterialsRepository;
    }




    public void mrpRun(){
        List<WorkOrder> listOfPlannedWorkOrders = workOrderRepository.findByStatus(WorkOrderStatus.PLANNED);
        HashMap<Component, Double> componentsRequired = calculateGrossRequirements(listOfPlannedWorkOrders);



    }


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

    private List<HashMap<Component,Double>> calculateNetRequirements(HashMap<Component, Double> componentsRequired){
        HashMap<Component, Double> missingComponentsToProduce = new HashMap<>();
        HashMap<Component, Double> missingComponentsToBuy = new HashMap<>();
        List<HashMap<Component,Double>> result = List.of(missingComponentsToBuy,missingComponentsToProduce);
        componentsRequired.forEach(
                (k,v) ->
                {
                    {
                        if(v < inventoryRepository.findByComponentId(k.getId()).getQtyAvailable() + inventoryRepository.findByComponentId(k.getId()).getQtyReserved())
                        {
                            if(billOfMaterialsRepository.findAllByProduct_Name(k.getName()) == null || billOfMaterialsRepository.findAllByProduct_Name(k.getName()).isEmpty())
                            {
                                missingComponentsToBuy.computeIfPresent(k, (k1,v1) -> (v1 += v));
                                missingComponentsToBuy.computeIfAbsent(k, v1 -> v);
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


}
