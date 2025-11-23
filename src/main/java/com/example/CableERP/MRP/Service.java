package com.example.CableERP.MRP;


import com.example.CableERP.WorkOrder.WorkOrderRepository;
import com.example.CableERP.WorkOrder.WorkOrderService;

@org.springframework.stereotype.Service
public class Service {


    private final WorkOrderRepository workOrderRepository;

    public Service(WorkOrderRepository workOrderRepository){
        this.workOrderRepository = workOrderRepository;
    }


    public void mrpRun(){

    }


    //TODO
    private void calculateGrossRequirements(){

    }

    //TODO
    private void calculateNetRequirements(){

    }

    //TODO
    private void planWorkOrders(){

    }

    //TODO
    private void planPurchaseOrders(){

    }


}
