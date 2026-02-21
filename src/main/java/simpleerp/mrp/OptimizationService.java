package simpleerp.mrp;

import simpleerp.customer.co.Order;
import simpleerp.customer.co.OrderItem;
import simpleerp.customer.co.OrderRepository;
import simpleerp.customer.co.OrderStatus;
import simpleerp.notification.NotificationService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class OptimizationService {

    private static final int MIN_PLANNED_START_GAP_MINUTES = 60;
    private final OrderRepository orderRepository;
    private final NotificationService notificationService;

    public OptimizationService(OrderRepository orderRepository, NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.notificationService = notificationService;
    }


    public void checkForOptimization(Order candidateOrder) {
        if (candidateOrder.getStatus() != OrderStatus.READY_FOR_PRODUCTION) {
            return;
        }

        Instant now = Instant.now();
        Instant plannedStart = candidateOrder.getPlannedStartAt().toInstant();

        if (Duration.between(now, plannedStart).toMinutes() < MIN_PLANNED_START_GAP_MINUTES) {
            return;
        }


        long durationMinutes = Duration.between(
                plannedStart,
                candidateOrder.getPlannedEndAt().toInstant()
        ).toMinutes();

        Order runningOrder = orderRepository.findFirstByStatus(OrderStatus.IN_PRODUCTION);
        Instant machineFreeAt = (runningOrder != null && runningOrder.getPlannedEndAt() != null)
                ? runningOrder.getPlannedEndAt().toInstant()
                : now;

        Order nextScheduledOrder = orderRepository.findFirstByStatusInAndPlannedStartAtAfterOrderByPlannedStartAtAsc(
                List.of(OrderStatus.WAITING_FOR_COMPONENTS, OrderStatus.READY_FOR_PRODUCTION, OrderStatus.IN_PRODUCTION),
                Timestamp.from(now)
        );
        if (nextScheduledOrder == null) {
            suggestJump(candidateOrder, "NOW (Machine Free)", durationMinutes);
            return;
        }
        if (nextScheduledOrder.getId().equals(candidateOrder.getId())) {
            suggestJump(candidateOrder, "NOW", durationMinutes);
            return;
        }


        Instant ceiling = nextScheduledOrder.getPlannedStartAt().toInstant();
        long gapMinutes = Duration.between(now, ceiling).toMinutes();

        if (gapMinutes > durationMinutes) {
            String msg = String.format(
                    "OPTIMIZATION: Order #%d is READY and takes %d min. " +
                            "Machine is waiting for Order #%d until %s. " +
                            "You can squeeze Order #%d in NOW!",
                    candidateOrder.getId(),
                    durationMinutes,
                    nextScheduledOrder.getId(),
                    nextScheduledOrder.getPlannedStartAt().toString(),
                    candidateOrder.getId()
            );
            notificationService.createAlert(msg);
        }
    }

    private void suggestJump(Order order, String availability, long duration) {
        String msg = String.format(
                "OPTIMIZATION: Order #%d (Planned: %s) is READY and takes only %d min. " +
                        "Machine is available %s. Start it early!",
                order.getId(),
                order.getPlannedStartAt(),
                duration,
                availability
        );
        notificationService.createAlert(msg);
    }


}