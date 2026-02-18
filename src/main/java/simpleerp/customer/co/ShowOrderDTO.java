package simpleerp.customer.co;

import java.util.List;

public record ShowOrderDTO(Order order, List<OrderItemDTO> orderItemList) {
}
