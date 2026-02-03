package com.example.CableERP.integration;

import com.example.CableERP.BillOfMaterials.BillOfMaterials;
import com.example.CableERP.BillOfMaterials.BillOfMaterialsRepository;
import com.example.CableERP.Component.Component;
import com.example.CableERP.Component.ComponentRepository;
import com.example.CableERP.Inventory.Inventory;
import com.example.CableERP.Vendor.ComponentVendor;
import com.example.CableERP.Vendor.ComponentVendorRepository;
import com.example.CableERP.Common.Unit;
import com.example.CableERP.Customer.Customer;
import com.example.CableERP.Customer.CustomerOrder.*;
import com.example.CableERP.Customer.CustomerRepository;
import com.example.CableERP.Inventory.InventoryRepository;
import com.example.CableERP.Inventory.InventoryService;
import com.example.CableERP.Product.Product;
import com.example.CableERP.Product.ProductRepository;
import com.example.CableERP.Vendor.Vendor;
import com.example.CableERP.Vendor.VendorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // Czyści bazę H2 przed każdym testem
public class OrderFlowIntegrationTest {

    @Autowired OrderService orderService;
    @Autowired InventoryService inventoryService;
    @Autowired InventoryRepository inventoryRepository;
    @Autowired CustomerRepository customerRepository;
    @Autowired ProductRepository productRepository;
    @Autowired ComponentRepository componentRepository;
    @Autowired VendorRepository vendorRepository;
    @Autowired ComponentVendorRepository componentVendorRepository;
    @Autowired BillOfMaterialsRepository bomRepository;
    @Autowired OrderRepository orderRepository;


    @Test
    void shouldAutomaticallyPromoteOrderToReady_WhenGoodsArrive() {
        // === 1. SETUP DANYCH (Baza danych) ===
        Customer customer = customerRepository.save(new Customer("Test Client", "123", "test@test.com"));

        Component wire = componentRepository.save(new Component("Wire", Unit.meter, 1.0));
        // Inicjalizacja pustego magazynu
        inventoryService.initializeOrUpdateInventory(new com.example.CableERP.Inventory.CreateInventoryDTO(wire.getId(), 0.0, 0.0));

        Vendor vendor = vendorRepository.save(new Vendor("Acme", "123", "a@a.com"));
        componentVendorRepository.save(new ComponentVendor(wire, vendor, 5, 1.0));

        Product cable = new Product("USB Cable", "Desc");
        cable.setMinutesToProduceOnePiece(10.0);
        productRepository.save(cable);

        // Tworzymy BOM: 1 Kabel wymaga 2 metrów drutu
        BillOfMaterials bom = new BillOfMaterials(cable, wire, 2.0);
        bomRepository.save(bom);
        // Odświeżamy relacje (ważne w testach integracyjnych)
        cable.getBillOfMaterialsList().add(bom);
        productRepository.save(cable);


        // === 2. AKCJA: Składamy zamówienie na 10 kabli ===
        // Potrzeba: 10 * 2 = 20 metrów drutu. Mamy 0.
        CreateItemsInOrderDTO itemDto = new CreateItemsInOrderDTO(cable.getId(), 10.0);
        Order order = orderService.placeOrder(customer.getId(), List.of(itemDto));

        // ASERCJA 1: Zamówienie powinno czekać na komponenty
        assertEquals(OrderStatus.WAITING_FOR_COMPONENTS, order.getStatus());

        // ASERCJA 2: Sprawdź rezerwacje
        var inventory = inventoryRepository.findByComponentId(wire.getId()).get();
        assertEquals(0.0, inventory.getQtyAvailable(), "Fizycznie 0");
        assertEquals(20.0, inventory.getQtyReserved(), "Zarezerwowane 20 (na minus)");


        // === 3. AKCJA: Przyjmujemy dostawę (Trigger FIFO) ===
        // Przyjmujemy 50 metrów. To wystarczy na pokrycie długu (20).
        inventoryService.receiveGoods(wire.getId(), 50.0);


        // === 4. WERYFIKACJA KOŃCOWA ===
        // Pobieramy zamówienie ponownie z bazy, żeby zobaczyć zmiany
        Order updatedOrder = orderRepository.findById(order.getId()).get();
        Inventory updatedInventory = inventoryRepository.findByComponentId(wire.getId()).get();

        // CZY STATUS SIĘ ZMIENIŁ?
        assertEquals(OrderStatus.READY_FOR_PRODUCTION, updatedOrder.getStatus(),
                "Zamówienie powinno automatycznie zmienić status na READY po dostawie!");

        // CZY STANY SĄ OK?
        // Available powinno być 50 (fizycznie)
        // Reserved powinno być 20 (nadal trzymamy rezerwację, ale teraz jest ona 'fulfilled')
        assertEquals(50.0, updatedInventory.getQtyAvailable());
        assertEquals(20.0, updatedInventory.getQtyReserved());
    }

}
