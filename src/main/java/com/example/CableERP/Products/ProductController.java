package com.example.CableERP.Products;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts(){
        // TODO: MAKE PAGEABLE
        return ResponseEntity
                    .ok()
                    .body(productService.getAllProducts());
    }




    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        System.out.println(product.toString());
            Product createdProduct = productService.addProduct(product);
            URI location = URI.create("/products/"+product.getId());
            return ResponseEntity
                    .created(location)
                    .location(location)
                    .body(createdProduct);
    }

}
