package com.example.CableERP.Order;

import com.example.CableERP.Product.Product;

public record CreateItemsInOrderDTO(Long productId, Double qty) {
}
