package simpleerp.unit;

import simpleerp.bom.BillOfMaterials;
import simpleerp.component.Component;
import simpleerp.customer.co.Order;
import simpleerp.customer.co.OrderItem;
import simpleerp.customer.co.OrderRepository;
import simpleerp.inventory.Inventory;
import simpleerp.inventory.InventoryRepository;
import simpleerp.mrp.EstimationService;
import simpleerp.product.Product;
import simpleerp.vendor.ComponentVendor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EstimationServiceTest {

    @Mock
    InventoryRepository inventoryRepository;
    @Mock
    OrderRepository orderRepository;

    EstimationService estimationService;

    @BeforeEach
    void setUp() {
        estimationService = new EstimationService(orderRepository);
    }

    @Test
    void shouldDelayStart_WhenMaterialIsMissing() {
        when(orderRepository.findFirstByPlannedEndAtIsNotNullOrderByPlannedEndAtDesc())
                .thenReturn(Optional.empty());

        Component copperWire = new Component("Copper", null, 10.0);

        int vendorLeadTimeDays = 7;
        ComponentVendor vendor = new ComponentVendor(copperWire, null, vendorLeadTimeDays, 5.0);

        vendor.setPreferred(true);
        copperWire.getComponentVendors().add(vendor);
        copperWire.setInventory(new Inventory(copperWire, 0.0, 0.0));

        Product cable = new Product("Cable", "Desc");
        cable.setMinutesToProduceOnePiece(10.0);

        BillOfMaterials bom = new BillOfMaterials(cable, copperWire, 1.0);
        cable.getBillOfMaterialsList().add(bom);

        OrderItem item = new OrderItem(null, cable, 1.0);
        List<Timestamp> result = estimationService.estimate(List.of(item));
        Timestamp start = result.get(0);
        Timestamp end = result.get(1);
        Instant now = Instant.now();

        Instant expectedStart = now.plus(vendorLeadTimeDays, ChronoUnit.DAYS);
        long diff = Math.abs(expectedStart.toEpochMilli() - start.getTime());


        // because of using Instant.now() i need to add tolerance so the test does not fail because of some milliseconds
        // note to self: Time Mocking with Clock instance
        long toleranceMs = 1000;
        assertTrue(diff < toleranceMs, "Start should be delayed by Vendor Lead Time (7 days). Diff: "
                + diff + "ms");

        long productionTimeInMinutes = 10;
        long expectedEnd = start.toInstant().plus(productionTimeInMinutes, ChronoUnit.MINUTES).toEpochMilli();
        assertEquals(expectedEnd, end.getTime(), "End time should be 10 min after start");
    }

    @Test
    void shouldWaitForMachine_WhenMaterialsAreReady() {
        Instant tomorrow = Instant.now().plus(1, ChronoUnit.DAYS);
        Order lastOrder = new Order(null, null);
        lastOrder.setPlannedEndAt(Timestamp.from(tomorrow));

        when(orderRepository.findFirstByPlannedEndAtIsNotNullOrderByPlannedEndAtDesc())
                .thenReturn(Optional.of(lastOrder));

        Component copperWire = new Component("Copper", null, 10.0);
        copperWire.setInventory(new Inventory(copperWire, 100.0, 0.0));

        Product cable = new Product("Cable", "Desc");
        cable.setMinutesToProduceOnePiece(60.0);

        BillOfMaterials bom = new BillOfMaterials(cable, copperWire, 1.0);
        cable.getBillOfMaterialsList().add(bom);

        OrderItem item = new OrderItem(null, cable, 1.0);
        List<Timestamp> result = estimationService.estimate(List.of(item));
        Timestamp start = result.get(0);

        assertEquals(tomorrow.getEpochSecond(), start.toInstant().getEpochSecond(),
                "Start time should match when the previous order finishes");
    }
}