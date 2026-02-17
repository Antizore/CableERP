package simpleerp.Reservation;

public record ReservationRequestDTO(
        Long orderId,
        Long componentId,
        double qty
) {}