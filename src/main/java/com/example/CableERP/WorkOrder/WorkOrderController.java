package com.example.CableERP.WorkOrder;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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



    @PostMapping("/{id}/start")
    public ResponseEntity<String> startWorkOrder(@PathVariable Long id){
        workOrderService.startWorkOrder(id);
        return ResponseEntity.ok().body("ok");
    }






}
