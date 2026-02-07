package com.example.CableERP.MRP;

import com.example.CableERP.BillOfMaterials.BillOfMaterials;
import com.example.CableERP.Component.Component;
import com.example.CableERP.Vendor.ComponentVendor;
import com.example.CableERP.Customer.CustomerOrder.Order;
import com.example.CableERP.Customer.CustomerOrder.OrderItem;
import com.example.CableERP.Customer.CustomerOrder.OrderRepository;
import com.example.CableERP.Inventory.Inventory;
import com.example.CableERP.Inventory.InventoryRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class EstimationService {

    private final OrderRepository orderRepository;

    public EstimationService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Main estimation method
     * Returns [Start, End].
     */
    public List<Timestamp> estimate(List<OrderItem> orderItemList) {
        Timestamp machineFreeAt = getMachineAvailability();
        double materialDelayDays = calculateMaxMaterialDelay(orderItemList);
        Timestamp plannedStart = calculateStartDate(machineFreeAt, materialDelayDays);
        Timestamp plannedEnd = calculateEndDate(plannedStart, orderItemList);
        return List.of(plannedStart, plannedEnd);
    }


    private Double calculateMaxMaterialDelay(List<OrderItem> orderItemList) {

        Map<Component, Double> totalNeeds = new HashMap<>();
        for (OrderItem item : orderItemList) {
            double productQty = item.getQty();
            for (BillOfMaterials bom : item.getProduct().getBillOfMaterialsList()) {
                totalNeeds.merge(bom.getComponent(), bom.getQty() * productQty, Double::sum);
            }
        }

        double maxDelayDays = 0.0;

        for (Map.Entry<Component, Double> entry : totalNeeds.entrySet()) {
            Component component = entry.getKey();
            Double neededQty = entry.getValue();
            Inventory inventory = component.getInventory();
            double available = (inventory != null) ? inventory.getQtyAvailable() : 0.0;
            double reserved = (inventory != null) ? inventory.getQtyReserved() : 0.0;
            double freeToUse = available - reserved;

            if (neededQty > freeToUse) {
                int leadTime = getLeadTimeForComponent(component);
                if (leadTime > maxDelayDays) {
                    maxDelayDays = leadTime;
                }
            }
        }
        return maxDelayDays;
    }

    private int getLeadTimeForComponent(Component component) {
        List<ComponentVendor> vendors = component.getComponentVendors();
        if (vendors == null || vendors.isEmpty()) {
            return 0;
        }

        return vendors.stream()
                .filter(ComponentVendor::isPreferred)
                .findFirst()
                .map(ComponentVendor::getLeadTimeDays)
                .orElse(vendors.stream()
                        .mapToInt(ComponentVendor::getLeadTimeDays)
                        .max()
                        .orElse(0));
    }



    private Timestamp getMachineAvailability() {
        return orderRepository.findFirstByPlannedEndAtIsNotNullOrderByPlannedEndAtDesc()
                .map(Order::getPlannedEndAt)
                .orElseGet(() -> Timestamp.from(Instant.now()));
    }


    private Timestamp calculateStartDate(Timestamp machineFreeAt, double delayDays) {
        Instant now = Instant.now();
        long delayMinutes = (long) (delayDays * 24 * 60);
        Instant materialsReadyAt = now.plus(delayMinutes, ChronoUnit.MINUTES);
        Instant machineReadyAt = machineFreeAt.toInstant();
        if (materialsReadyAt.isAfter(machineReadyAt)) {
            return Timestamp.from(materialsReadyAt);
        } else {
            return machineReadyAt.isAfter(now) ? Timestamp.from(machineReadyAt) : Timestamp.from(now);
        }
    }

    private Timestamp calculateEndDate(Timestamp startDate, List<OrderItem> orderItemList) {
        double totalMinutesToProduce = 0;

        for (OrderItem item : orderItemList) {
            Double timePerUnit = item.getProduct().getMinutesToProduceOnePiece();
            if (timePerUnit == null) timePerUnit = 0.0;
            totalMinutesToProduce += timePerUnit * item.getQty();
        }
        Instant start = startDate.toInstant();
        Instant end = start.plus((long) totalMinutesToProduce, ChronoUnit.MINUTES);
        return Timestamp.from(end);
    }
}