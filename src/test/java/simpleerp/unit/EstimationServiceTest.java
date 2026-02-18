package simpleerp.unit;

import simpleerp.BillOfMaterials.BillOfMaterials;
import simpleerp.component.Component;
import simpleerp.Customer.CustomerOrder.Order;
import simpleerp.Customer.CustomerOrder.OrderItem;
import simpleerp.Customer.CustomerOrder.OrderRepository;
import simpleerp.Inventory.Inventory;
import simpleerp.Inventory.InventoryRepository;
import simpleerp.MRP.EstimationService;
import simpleerp.Product.Product;
import simpleerp.Vendor.ComponentVendor;
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
        ComponentVendor vendor = new ComponentVendor(copperWire, null, 7, 5.0);

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
        Instant expectedStart = now.plus(7, ChronoUnit.DAYS);
        long diff = Math.abs(expectedStart.toEpochMilli() - start.getTime());

        assertTrue(diff < 1000, "Start should be delayed by Vendor Lead Time (7 days). Diff: " + diff + "ms");
        long expectedEnd = start.toInstant().plus(10, ChronoUnit.MINUTES).toEpochMilli();
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