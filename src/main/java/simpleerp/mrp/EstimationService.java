package simpleerp.mrp;

import simpleerp.bom.BillOfMaterials;
import simpleerp.component.Component;
import simpleerp.vendor.ComponentVendor;
import simpleerp.customer.co.Order;
import simpleerp.customer.co.OrderItem;
import simpleerp.customer.co.OrderRepository;
import simpleerp.inventory.Inventory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

        Map<Component, BigDecimal> totalNeeds = new HashMap<>();
        for (OrderItem item : orderItemList) {
            BigDecimal productQty = item.getQty();
            for (BillOfMaterials bom : item.getProduct().getBillOfMaterialsList()) {
                totalNeeds.merge(bom.getComponent(), productQty.multiply(bom.getQty()), BigDecimal::add);
            }
        }

        double maxDelayDays = 0.0;

        for (Map.Entry<Component, BigDecimal> entry : totalNeeds.entrySet()) {
            Component component = entry.getKey();
            BigDecimal neededQty = entry.getValue();
            Inventory inventory = component.getInventory();
            BigDecimal available = (inventory != null) ? inventory.getQtyAvailable() : BigDecimal.ZERO;
            BigDecimal reserved = (inventory != null) ? inventory.getQtyReserved() : BigDecimal.ZERO;
            BigDecimal freeToUse = available.subtract(reserved);

            if (neededQty.compareTo(freeToUse) > 0) {
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
        BigDecimal totalMinutesToProduce = BigDecimal.ZERO;

        for (OrderItem item : orderItemList) {
            BigDecimal timePerUnit = item.getProduct().getMinutesToProduceOnePiece();
            if (timePerUnit == null) timePerUnit = BigDecimal.ZERO;

            totalMinutesToProduce = totalMinutesToProduce.add(
                    item.getQty().multiply(timePerUnit)
            );
        }
        Instant start = startDate.toInstant();
        Instant end = start.plus(totalMinutesToProduce.longValue(), ChronoUnit.MINUTES);
        return Timestamp.from(end);
    }
}