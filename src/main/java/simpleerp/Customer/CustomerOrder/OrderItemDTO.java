package simpleerp.Customer.CustomerOrder;

import simpleerp.Product.ProductCreateDTO;

public record OrderItemDTO(Long id,ProductCreateDTO productCreateDTO, Double qty) {
}
