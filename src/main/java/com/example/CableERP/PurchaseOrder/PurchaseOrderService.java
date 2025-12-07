package com.example.CableERP.PurchaseOrder;


import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository){
        this.purchaseOrderRepository = purchaseOrderRepository;
    }


    public void orderPurchase(PurchaseOrder purchaseOrder){
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
    }




}
