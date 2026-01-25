package com.example.CableERP.MRP;

import com.example.CableERP.BillOfMaterials.BillOfMaterialsForEstimate;
import com.example.CableERP.Component.Component;
import com.example.CableERP.Customer.CustomerOrder.OrderItem;
import com.example.CableERP.Inventory.InventoryRepository;
import com.example.CableERP.WorkOrder.WorkOrderRepository;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;


public class Estimation {

    private final InventoryRepository inventoryRepository;
    private final WorkOrderRepository workOrderRepository;

    public Estimation(InventoryRepository inventoryRepository, WorkOrderRepository workOrderRepository) {
        this.inventoryRepository = inventoryRepository;
        this.workOrderRepository = workOrderRepository;
    }

    public Double estimateComponentsAvailiability(List<OrderItem> orderItemList) {

        Map<Component, Double> summaricOfComponentNeeds = new HashMap<>();
        for (OrderItem orderItem : orderItemList) {

            // prod time orderItem.getProduct().getMinutesToProduceOnePiece() * orderItem.getQty();

            List<BillOfMaterialsForEstimate> listOfNeededComponents = orderItem.getProduct().getBillOfMaterialsList().
                    stream().map(
                            b -> new BillOfMaterialsForEstimate(b.getQty(), new Component(b.getComponent().getName(),
                                    b.getComponent().getUnit(), b.getComponent().getCostPerUnit()))
                    ).toList();

            for (BillOfMaterialsForEstimate bill : listOfNeededComponents) {
                summaricOfComponentNeeds.putIfAbsent(bill.component(), bill.qty() * orderItem.getQty());
                summaricOfComponentNeeds.computeIfPresent(bill.component(), (k, v) -> v += bill.qty()
                        * orderItem.getQty());
            }
        }

        // let's check with inventory how our components are doing and if we have to wait for some of them
        // this is a map containing component and its availability in days
        // 0 means that there are enough components to start production
        // value > 0 means that you need make order for procurement and need to wait for shipment (lead time days)
        Map<Component, Double> componentAvailability = new HashMap<>();

        summaricOfComponentNeeds.forEach(
                (k, v) -> {
                    Double availableQty = inventoryRepository.findByComponentId(k.getId()).getQtyAvailable();
                    if (v > availableQty) {
                        componentAvailability.put(k, k.getProcurement().getLeadTimeDays());
                    } else {
                        componentAvailability.put(k, 0.);
                    }
                }
        );
        //you can start only when all components are available so need to know te maximum waiting time
        return Collections.max(componentAvailability.values());
    }


}
