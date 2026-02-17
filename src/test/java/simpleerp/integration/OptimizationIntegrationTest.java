package simpleerp.integration;

import simpleerp.Customer.CustomerOrder.CreateItemsInOrderDTO;
import simpleerp.Customer.CustomerOrder.Order;
import simpleerp.Customer.CustomerOrder.OrderService;
import simpleerp.Notification.Notification;
import simpleerp.Notification.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OptimizationIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    @Transactional
    void shouldCreateOptimizationAlert_WhenSmallOrderFitsInScheduleGap() {

        Long existingCustomerId = 1L;
        Long fastProductionProductId = 4L;

        CreateItemsInOrderDTO itemDto = new CreateItemsInOrderDTO(fastProductionProductId, 1.0);
        Order candidateOrder = orderService.placeOrder(existingCustomerId, List.of(itemDto));

        List<Notification> alerts = notificationRepository.findAll();

        Notification generatedAlert = alerts.stream()
                .filter(n -> n.getMessage() != null)
                .filter(n -> n.getMessage().contains("Order #" + candidateOrder.getId()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Optimization alert not found for Order #" + candidateOrder.getId()));

        assertTrue(generatedAlert.getMessage().contains("OPTIMIZATION"));
        assertTrue(generatedAlert.getMessage().contains("squeeze Order #" + candidateOrder.getId()));
    }
}