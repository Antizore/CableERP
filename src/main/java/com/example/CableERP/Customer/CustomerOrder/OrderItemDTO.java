package com.example.CableERP.Customer.CustomerOrder;

import com.example.CableERP.Product.ProductCreateDTO;

public record OrderItemDTO(ProductCreateDTO productCreateDTO, Double qty) {
}
