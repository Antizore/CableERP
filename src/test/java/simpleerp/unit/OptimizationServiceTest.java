package simpleerp.unit;

import simpleerp.customer.co.Order;
import simpleerp.customer.co.OrderItem;
import simpleerp.customer.co.OrderRepository;
import simpleerp.customer.co.OrderStatus;
import simpleerp.mrp.OptimizationService;
import simpleerp.notification.NotificationService;
import simpleerp.product.Product;
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
        Order candidate = createOrder(1L, OrderStatus.READY_FOR_PRODUCTION);
        setPlannedSchedule(candidate, Instant.now().plus(2, ChronoUnit.DAYS), 30L);

        Order blocker = createOrder(2L, OrderStatus.WAITING_FOR_COMPONENTS);
        setPlannedSchedule(blocker, Instant.now().plus(5, ChronoUnit.HOURS), 100L);

        when(orderRepository.findFirstByStatus(OrderStatus.IN_PRODUCTION)).thenReturn(null);
        when(orderRepository.findFirstByStatusInAndPlannedStartAtAfterOrderByPlannedStartAtAsc(any(), any()))
                .thenReturn(blocker);

        optimizationService.checkForOptimization(candidate);

        verify(notificationService, times(1)).createAlert(contains("OPTIMIZATION"));
    }


    @Test
    void shouldTrigger_WhenMachineIsCompletelyFree() {
        Order candidate = createOrder(1L, OrderStatus.READY_FOR_PRODUCTION);
        setPlannedSchedule(candidate, Instant.now().plus(1, ChronoUnit.DAYS), 60);

        when(orderRepository.findFirstByStatus(OrderStatus.IN_PRODUCTION)).thenReturn(null);
        when(orderRepository.findFirstByStatusInAndPlannedStartAtAfterOrderByPlannedStartAtAsc(any(), any()))
                .thenReturn(null);

        optimizationService.checkForOptimization(candidate);

        verify(notificationService).createAlert(contains("NOW"));
    }

    @Test
    void shouldTrigger_WhenMachineOccupiedButGapSufficient() {

        Order candidate = createOrder(1L, OrderStatus.READY_FOR_PRODUCTION);
        setPlannedSchedule(candidate, Instant.now().plus(1, ChronoUnit.DAYS), 60);


        Order runningOrder = createOrder(2L, OrderStatus.IN_PRODUCTION);
        setPlannedSchedule(runningOrder, Instant.now().minus(1, ChronoUnit.HOURS), 180); // Trwa 3h, wystartowało 1h temu


        Order nextScheduled = createOrder(3L, OrderStatus.WAITING_FOR_COMPONENTS);
        setPlannedSchedule(nextScheduled, Instant.now().plus(5, ChronoUnit.HOURS), 100);

        when(orderRepository.findFirstByStatus(OrderStatus.IN_PRODUCTION)).thenReturn(runningOrder);
        when(orderRepository.findFirstByStatusInAndPlannedStartAtAfterOrderByPlannedStartAtAsc(any(), any()))
                .thenReturn(nextScheduled);

        optimizationService.checkForOptimization(candidate);


        verify(notificationService).createAlert(contains("Order #3"));
    }


    private Order createOrder(Long id, OrderStatus status) {
        Order order = new Order(null, status);
        ReflectionTestUtils.setField(order, "id", id);
        return order;
    }

    private void setPlannedSchedule(Order order, Instant start, long durationMinutes) {
        order.setPlannedStartAt(Timestamp.from(start));
        order.setPlannedEndAt(Timestamp.from(start.plus(durationMinutes, ChronoUnit.MINUTES)));
    }
}