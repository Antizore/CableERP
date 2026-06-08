package simpleerp.reservation;

import java.math.BigDecimal;

public record ReservationRequestDTO(
        Long orderId,
        Long componentId,
        BigDecimal qty
) {}