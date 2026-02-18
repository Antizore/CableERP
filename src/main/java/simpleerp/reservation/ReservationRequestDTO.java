package simpleerp.reservation;

public record ReservationRequestDTO(
        Long orderId,
        Long componentId,
        double qty
) {}