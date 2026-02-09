package com.example.SimpleERP.MRP;

import com.example.SimpleERP.Customer.CustomerOrder.Order;
import com.example.SimpleERP.Customer.CustomerOrder.OrderItem;
import com.example.SimpleERP.Customer.CustomerOrder.OrderRepository;
import com.example.SimpleERP.Customer.CustomerOrder.OrderStatus;
import com.example.SimpleERP.Notification.NotificationService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class OptimizationService {

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

        if (Duration.between(now, plannedStart).toMinutes() < 60) {
            return;
        }

        long durationMinutes = calculateDurationMinutes(candidateOrder);
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

    private long calculateDurationMinutes(Order order) {
        double totalMinutes = 0;
        for (OrderItem item : order.getOrderItemList()) {
            Double prodTime = item.getProduct().getMinutesToProduceOnePiece();
            if (prodTime != null) {
                totalMinutes += prodTime * item.getQty();
            }
        }
        return (long) Math.ceil(totalMinutes * 1.1);
    }
}