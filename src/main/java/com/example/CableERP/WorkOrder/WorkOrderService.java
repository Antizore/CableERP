package com.example.CableERP.WorkOrder;

import org.springframework.stereotype.Service;

@Service
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;

    public WorkOrderService(WorkOrderRepository workOrderRepository){
        this.workOrderRepository = workOrderRepository;
    }



    public void createWorkOrder(){

    }

    public void startWorkOrder(){

    }

    public void finishWorkOrder(){

    }

    public void calculateRequiredComponents(){

    }




}
