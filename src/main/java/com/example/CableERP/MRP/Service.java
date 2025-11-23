package com.example.CableERP.MRP;


import com.example.CableERP.WorkOrder.WorkOrder;
import com.example.CableERP.WorkOrder.WorkOrderRepository;
import com.example.CableERP.WorkOrder.WorkOrderService;
import com.example.CableERP.WorkOrder.WorkOrderStatus;

import java.util.List;

@org.springframework.stereotype.Service
public class Service {


    private final WorkOrderRepository workOrderRepository;

    public Service(WorkOrderRepository workOrderRepository){
        this.workOrderRepository = workOrderRepository;
    }


    public void mrpRun(){
        List<WorkOrder> listOfPlannedWorkOrders = workOrderRepository.findByStatus(WorkOrderStatus.PLANNED);
        for(WorkOrder workOrder : listOfPlannedWorkOrders){
            System.out.println(workOrder.toString());
        }
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
