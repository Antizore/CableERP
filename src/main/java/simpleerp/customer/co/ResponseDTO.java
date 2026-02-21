package simpleerp.customer.co;

import java.sql.Timestamp;

public record ResponseDTO(
        Long id,
        OrderStatus status,
        Timestamp plannedStartAt,
        Timestamp plannedEndAt
) {
}
