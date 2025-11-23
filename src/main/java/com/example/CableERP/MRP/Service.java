package com.example.CableERP.MRP;


import com.example.CableERP.BillOfMaterials.BillOfMaterials;
import com.example.CableERP.Component.Component;
import com.example.CableERP.WorkOrder.WorkOrder;
import com.example.CableERP.WorkOrder.WorkOrderRepository;
import com.example.CableERP.WorkOrder.WorkOrderService;
import com.example.CableERP.WorkOrder.WorkOrderStatus;

import java.util.HashMap;
import java.util.List;

@org.springframework.stereotype.Service
public class Service {


    private final WorkOrderRepository workOrderRepository;

    public Service(WorkOrderRepository workOrderRepository){
        this.workOrderRepository = workOrderRepository;
    }




    public void mrpRun(){
        List<WorkOrder> listOfPlannedWorkOrders = workOrderRepository.findByStatus(WorkOrderStatus.PLANNED);
        System.out.println(calculateGrossRequirements(listOfPlannedWorkOrders).toString());




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

    /*
    TODO
     liczymy zapotrzebowanie net = gross - qty_available - qty_reserved i sprawdzamy czy są braki
     wynikiem ma być tylko lista komponentów z brakami
     { component_id: 12, missing: 80 },
     { component_id: 7, missing: 15 }
     */
    private void calculateNetRequirements(){

    }

    //TODO
    private void planWorkOrders(){

    }

    //TODO
    private void planPurchaseOrders(){

    }


}
