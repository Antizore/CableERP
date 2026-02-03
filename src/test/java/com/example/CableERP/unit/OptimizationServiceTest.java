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

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OptimizationServiceTest {

    @Mock OrderRepository orderRepository;
    @Mock NotificationService notificationService;

    OptimizationService optimizationService;

    @BeforeEach
    void setUp() {
        optimizationService = new OptimizationService(orderRepository, notificationService);
    }

    @Test
    void shouldTriggerAlert_WhenGapIsLargeEnough() {
        // GIVEN
        // 1. Kandydat: Małe zamówienie (30 min), gotowe (READY), ale zaplanowane na za 2 dni
        Order candidate = createOrder(1L, OrderStatus.READY_FOR_PRODUCTION, 30.0); // 30 min prod
        setPlannedStart(candidate, Instant.now().plus(2, ChronoUnit.DAYS));

        // 2. Sytuacja na maszynie: Następne duże zamówienie startuje dopiero za 5 godzin
        // Mamy więc lukę ~5h. Nasze 30 min się zmieści.
        Order blocker = createOrder(2L, OrderStatus.WAITING_FOR_COMPONENTS, 100.0);
        setPlannedStart(blocker, Instant.now().plus(5, ChronoUnit.HOURS));

        when(orderRepository.findFirstByStatusInAndPlannedStartAtAfterOrderByPlannedStartAtAsc(any(), any()))
                .thenReturn(blocker);

        // WHEN
        optimizationService.checkForOptimization(candidate);

        // THEN
        verify(notificationService, times(1)).createAlert(contains("OPTIMIZATION"));
    }

    @Test
    void shouldNotTrigger_WhenGapIsTooSmall() {
        // GIVEN
        // 1. Kandydat: Potrzebuje 2 godzin (120 min)
        Order candidate = createOrder(1L, OrderStatus.READY_FOR_PRODUCTION, 120.0);
        setPlannedStart(candidate, Instant.now().plus(2, ChronoUnit.DAYS));

        // 2. Sytuacja: Następne zamówienie startuje za 1 godzinę.
        // Luka (1h) < Potrzeba (2h). Nie zmieścimy się.
        Order blocker = createOrder(2L, OrderStatus.WAITING_FOR_COMPONENTS, 100.0);
        setPlannedStart(blocker, Instant.now().plus(1, ChronoUnit.HOURS));

        when(orderRepository.findFirstByStatusInAndPlannedStartAtAfterOrderByPlannedStartAtAsc(any(), any()))
                .thenReturn(blocker);

        // WHEN
        optimizationService.checkForOptimization(candidate);

        // THEN
        verify(notificationService, never()).createAlert(anyString());
    }

    @Test
    void shouldTrigger_WhenMachineIsCompletelyFree() {
        // GIVEN
        // Kandydat gotowy, ale zaplanowany na jutro (bo np. tak wyszło z FIFO wcześniej)
        Order candidate = createOrder(1L, OrderStatus.READY_FOR_PRODUCTION, 60.0);
        setPlannedStart(candidate, Instant.now().plus(1, ChronoUnit.DAYS));

        // Maszyna nie ma żadnych planów w przyszłości (null)
        when(orderRepository.findFirstByStatusInAndPlannedStartAtAfterOrderByPlannedStartAtAsc(any(), any()))
                .thenReturn(null);

        // WHEN
        optimizationService.checkForOptimization(candidate);

        // THEN
        verify(notificationService).createAlert(contains("Machine Free"));
    }

    // --- Helpers ---

    private Order createOrder(Long id, OrderStatus status, double minutesToProduce) {
        // Używamy refleksji lub setera (jeśli jest) dla ID, tu uproszczenie
        // Zakładam, że masz konstruktor lub settery
        Product product = new Product("TestProd", "Desc");
        product.setMinutesToProduceOnePiece(minutesToProduce);

        Order order = new Order(null, status);
        // Symulacja ID (wymaga, żeby ID było ustawialne lub mockowane, tu hack na potrzeby przykładu)
        // Jeśli ID jest private, w teście jednostkowym mockito może je wstrzyknąć, lub używamy spy.
        // Dla uproszczenia zakładam, że hashCode/equals nie polega na ID w logice serwisu,
        // ale w logice serwisu używamy order.getId().
        // W prawdziwym teście użyłbym ReflectionTestUtils.setField(order, "id", id);
        try {
            java.lang.reflect.Field idField = Order.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(order, id);
        } catch (Exception e) { throw new RuntimeException(e); }

        OrderItem item = new OrderItem(order, product, 1.0);
        order.getOrderItemList().add(item);

        return order;
    }

    private void setPlannedStart(Order order, Instant start) {
        order.setPlannedStartAt(Timestamp.from(start));
    }
}