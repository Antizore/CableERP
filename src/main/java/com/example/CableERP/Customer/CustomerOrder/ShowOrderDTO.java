package com.example.CableERP.Customer.CustomerOrder;

import java.util.List;

public record ShowOrderDTO(Order order, List<OrderItem> orderItemList) {
}
