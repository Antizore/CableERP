package com.example.CableERP.Order;

import java.util.List;

public record ShowOrderDTO(Order order, List<OrderItem> orderItemList) {
}
