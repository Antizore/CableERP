package com.example.CableERP.Customer.CustomerOrder;

import com.example.CableERP.Product.ProductCreateDTO;

public record OrderItemDTO(Long id,ProductCreateDTO productCreateDTO, Double qty) {
}
