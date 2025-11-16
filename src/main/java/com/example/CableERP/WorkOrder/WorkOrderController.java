package com.example.CableERP.WorkOrder;

import com.example.CableERP.WorkOrder.CreateWorkOrderResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/work-orders")
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    public WorkOrderController(WorkOrderService workOrderService){
        this.workOrderService = workOrderService;
    }


    @PostMapping
    public ResponseEntity<CreateWorkOrderResponseDTO> createWorkOrder(@RequestBody CreateWorkOrderRequestDTO createWorkOrderDTO){
        return ResponseEntity
                .ok()
                .body(workOrderService.createWorkOrder(createWorkOrderDTO));
    }






}
