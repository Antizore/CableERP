package simpleerp.reservation;

import java.math.BigDecimal;

public record ReservationResponseDTO(
        Long id,
        BigDecimal qty,
        boolean isFulfilled
) {
}
