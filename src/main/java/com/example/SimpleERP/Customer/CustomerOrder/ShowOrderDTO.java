package com.example.SimpleERP.Customer.CustomerOrder;

import java.util.List;

public record ShowOrderDTO(Order order, List<OrderItemDTO> orderItemList) {
}
