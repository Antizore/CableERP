package simpleerp.Reservation;

public record ReservationResponseDTO(
        Long id,
        double qty,
        boolean isFulfilled
) {
}
