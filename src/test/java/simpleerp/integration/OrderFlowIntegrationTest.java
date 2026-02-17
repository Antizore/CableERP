package simpleerp.integration;

import simpleerp.Customer.CustomerOrder.*;
import simpleerp.Customer.CustomerOrder.*;
import simpleerp.Inventory.Inventory;
import simpleerp.Inventory.InventoryRepository;
import simpleerp.Inventory.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrderFlowIntegrationTest {

    @Autowired private OrderService orderService;
    @Autowired private InventoryService inventoryService;
    @Autowired private OrderRepository orderRepository;
    @Autowired private InventoryRepository inventoryRepository;

    @Test
    @Transactional
    void shouldAutomaticallyPromoteOrderToReady_WhenGoodsArrive() {
        Long existingCustomerId = 1L;
        Long productWithNoStockId = 3L;
        Long componentForProductId = 30L;

        CreateItemsInOrderDTO itemDto = new CreateItemsInOrderDTO(productWithNoStockId, 10.0);
        Order order = orderService.placeOrder(existingCustomerId, List.of(itemDto));

        assertEquals(OrderStatus.WAITING_FOR_COMPONENTS, order.getStatus());

        Inventory initialInventory = inventoryRepository.findByComponentId(componentForProductId).orElseThrow();
        assertEquals(0.0, initialInventory.getQtyAvailable());
        assertEquals(10.0, initialInventory.getQtyReserved());

        inventoryService.receiveGoods(componentForProductId, 50.0);

        Order updatedOrder = orderRepository.findById(order.getId()).orElseThrow();
        Inventory updatedInventory = inventoryRepository.findByComponentId(componentForProductId).orElseThrow();

        assertEquals(OrderStatus.READY_FOR_PRODUCTION, updatedOrder.getStatus());
        assertEquals(50.0, updatedInventory.getQtyAvailable());
        assertEquals(10.0, updatedInventory.getQtyReserved());
    }
}