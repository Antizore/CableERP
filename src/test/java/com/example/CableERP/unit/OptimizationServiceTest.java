package com.example.CableERP.unit;

import com.example.CableERP.Customer.CustomerOrder.Order;
import com.example.CableERP.Customer.CustomerOrder.OrderItem;
import com.example.CableERP.Customer.CustomerOrder.OrderRepository;
import com.example.CableERP.Customer.CustomerOrder.OrderStatus;
import com.example.CableERP.MRP.OptimizationService;
import com.example.CableERP.Notification.NotificationService;
import com.example.CableERP.Product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OptimizationServiceTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    NotificationService notificationService;

    OptimizationService optimizationService;

    @BeforeEach
    void setUp() {
        optimizationService = new OptimizationService(orderRepository, notificationService);
    }

    @Test
    void shouldTriggerAlert_WhenGapIsLargeEnough() {
        Order candidate = createOrder(1L, OrderStatus.READY_FOR_PRODUCTION, 30.0);
        setPlannedStart(candidate, Instant.now().plus(2, ChronoUnit.DAYS));

        Order blocker = createOrder(2L, OrderStatus.WAITING_FOR_COMPONENTS, 100.0);
        setPlannedStart(blocker, Instant.now().plus(5, ChronoUnit.HOURS));

        when(orderRepository.findFirstByStatusInAndPlannedStartAtAfterOrderByPlannedStartAtAsc(any(), any()))
                .thenReturn(blocker);
        optimizationService.checkForOptimization(candidate);
        verify(notificationService, times(1)).createAlert(contains("OPTIMIZATION"));
    }

    @Test
    void shouldNotTrigger_WhenGapIsTooSmall() {
        Order candidate = createOrder(1L, OrderStatus.READY_FOR_PRODUCTION, 120.0);
        setPlannedStart(candidate, Instant.now().plus(2, ChronoUnit.DAYS));

        Order blocker = createOrder(2L, OrderStatus.WAITING_FOR_COMPONENTS, 100.0);
        setPlannedStart(blocker, Instant.now().plus(1, ChronoUnit.HOURS));

        when(orderRepository.findFirstByStatusInAndPlannedStartAtAfterOrderByPlannedStartAtAsc(any(), any()))
                .thenReturn(blocker);
        optimizationService.checkForOptimization(candidate);
        verify(notificationService, never()).createAlert(anyString());
    }

    @Test
    void shouldTrigger_WhenMachineIsCompletelyFree() {
        Order candidate = createOrder(1L, OrderStatus.READY_FOR_PRODUCTION, 60.0);
        setPlannedStart(candidate, Instant.now().plus(1, ChronoUnit.DAYS));

        when(orderRepository.findFirstByStatusInAndPlannedStartAtAfterOrderByPlannedStartAtAsc(any(), any()))
                .thenReturn(null);
        optimizationService.checkForOptimization(candidate);
        verify(notificationService).createAlert(contains("Machine Free"));
    }

    private Order createOrder(Long id, OrderStatus status, double minutesToProduce) {
        Product product = new Product("TestProd", "Desc");
        product.setMinutesToProduceOnePiece(minutesToProduce);

        Order order = new Order(null, status);
        ReflectionTestUtils.setField(order, "id", id);

        OrderItem item = new OrderItem(order, product, 1.0);
        order.getOrderItemList().add(item);
        return order;
    }

    private void setPlannedStart(Order order, Instant start) {
        order.setPlannedStartAt(Timestamp.from(start));
    }
}