package simpleerp.customer.co;

import simpleerp.product.ProductCreateDTO;

public record OrderItemDTO(Long id,ProductCreateDTO productCreateDTO, Double qty) {
}
