package com.example.CableERP.PurchaseOrder;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/purchase-orders")
public class PurchaseOrderController {


    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService){
        this.purchaseOrderService = purchaseOrderService;
    }


    @PostMapping
    public void createPurchaseOrder(@RequestBody PurchaseOrder purchaseOrder){
        
    }




}
