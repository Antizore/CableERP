package com.example.CableERP.unit;

import com.example.CableERP.BillOfMaterials.BillOfMaterials;
import com.example.CableERP.Component.Component;
import com.example.CableERP.Vendor.ComponentVendor;
import com.example.CableERP.Customer.CustomerOrder.Order;
import com.example.CableERP.Customer.CustomerOrder.OrderItem;
import com.example.CableERP.Customer.CustomerOrder.OrderRepository;
import com.example.CableERP.Inventory.Inventory;
import com.example.CableERP.Inventory.InventoryRepository;
import com.example.CableERP.MRP.EstimationService;
import com.example.CableERP.Product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class EstimationServiceTest {

    @Mock InventoryRepository inventoryRepository;
    @Mock OrderRepository orderRepository;


    EstimationService estimationService;

    @BeforeEach
    void setUp() {
        estimationService = new EstimationService(orderRepository);
    }

    @Test
    void shouldDelayStart_WhenMaterialIsMissing() {
        // GIVEN
        // 1. Maszyna jest wolna od "TERAZ"
        when(orderRepository.findFirstByPlannedEndAtIsNotNullOrderByPlannedEndAtDesc())
                .thenReturn(Optional.empty());

        // 2. Produkt wymaga Komponentu, którego nie ma
        Component copperWire = new Component("Copper", null, 10.0);
        // Vendor dostarcza w 7 dni
        ComponentVendor vendor = new ComponentVendor(copperWire, null, 7, 5.0);
        vendor.setPreferred(true);
        copperWire.getComponentVendors().add(vendor);
        // Inventory puste (Avail: 0, Reserved: 0)
        copperWire.setInventory(new Inventory(copperWire, 0, 0));

        Product cable = new Product("Cable", "Desc");
        cable.setMinutesToProduceOnePiece(10.0); // 10 min produkcji
        BillOfMaterials bom = new BillOfMaterials(cable, copperWire, 1.0);
        cable.getBillOfMaterialsList().add(bom);

        OrderItem item = new OrderItem(null, cable, 1.0);

        // WHEN
        List<Timestamp> result = estimationService.estimate(List.of(item));

        // THEN
        Timestamp start = result.get(0);
        Timestamp end = result.get(1);

        Instant now = Instant.now();
        Instant expectedStart = now.plus(7, ChronoUnit.DAYS);

        // Sprawdzamy z tolerancją 1 sekundy (bo test trwa ułamki sekund)
        assertTrue(Math.abs(expectedStart.toEpochMilli() - start.getTime()) < 1000,
                "Start powinien być za 7 dni (Lead Time)");

        // Koniec = Start + 10 minut
        assertEquals(start.toInstant().plus(10, ChronoUnit.MINUTES).toEpochMilli(), end.getTime(),
                "Koniec powinien być 10 min po starcie");
    }

    @Test
    void shouldWaitForMachine_WhenMaterialsAreReady() {
        // GIVEN
        // 1. Maszyna zajęta do JUTRA
        Instant tomorrow = Instant.now().plus(1, ChronoUnit.DAYS);
        Order lastOrder = new Order(null, null);
        lastOrder.setPlannedEndAt(Timestamp.from(tomorrow));

        when(orderRepository.findFirstByPlannedEndAtIsNotNullOrderByPlannedEndAtDesc())
                .thenReturn(Optional.of(lastOrder));

        // 2. Materiały są dostępne (Inventory > Needed)
        Component copperWire = new Component("Copper", null, 10.0);
        copperWire.setInventory(new Inventory(copperWire, 100, 0)); // Mamy 100 sztuk

        Product cable = new Product("Cable", "Desc");
        cable.setMinutesToProduceOnePiece(60.0);
        BillOfMaterials bom = new BillOfMaterials(cable, copperWire, 1.0);
        cable.getBillOfMaterialsList().add(bom);

        OrderItem item = new OrderItem(null, cable, 1.0);

        // WHEN
        List<Timestamp> result = estimationService.estimate(List.of(item));

        // THEN
        Timestamp start = result.get(0);

        // Start musi być równy dacie zwolnienia maszyny (Jutro), a nie "Teraz"
        assertEquals(tomorrow.toEpochMilli(), start.getTime());
    }

}
