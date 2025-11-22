package com.example.CableERP.WorkOrder;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/work-orders")
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    public WorkOrderController(WorkOrderService workOrderService){
        this.workOrderService = workOrderService;
    }



    @GetMapping
    public ResponseEntity<List<ReturnAllWorkOrdersDTO>> getAllWorkOrders(){
        return ResponseEntity
                .ok()
                .body(workOrderService.getAllWorkOrders());
    }



    @PostMapping
    public ResponseEntity<CreateWorkOrderResponseDTO> createWorkOrder(@RequestBody CreateWorkOrderRequestDTO createWorkOrderDTO){
        return ResponseEntity
                .ok()
                .body(workOrderService.createWorkOrder(createWorkOrderDTO));
    }



    //TODO: senowny response
    @PostMapping("/{id}/start")
    public ResponseEntity<String> startWorkOrder(@PathVariable Long id){
        workOrderService.startWorkOrder(id);
        return ResponseEntity.ok().body("ok");
    }


    //TODO: senowny response
    @PostMapping("/{id}/finish")
    public ResponseEntity<String> endWorkOrder(@PathVariable Long id){
        workOrderService.finishWorkOrder(id);
        return ResponseEntity.ok().body("ok");
    }







}
