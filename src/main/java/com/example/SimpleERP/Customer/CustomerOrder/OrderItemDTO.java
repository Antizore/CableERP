package com.example.SimpleERP.Customer.CustomerOrder;

import com.example.SimpleERP.Product.ProductCreateDTO;

public record OrderItemDTO(Long id,ProductCreateDTO productCreateDTO, Double qty) {
}
