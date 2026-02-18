package simpleerp.reservation;

public record ReservationResponseDTO(
        Long id,
        double qty,
        boolean isFulfilled
) {
}
