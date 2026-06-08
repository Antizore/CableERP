package simpleerp.customer.co;

import java.math.BigDecimal;

public record CreateItemsInOrderDTO(Long productId, BigDecimal qty) {
}
