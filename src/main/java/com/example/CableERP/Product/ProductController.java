package com.example.CableERP.Product;


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


    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@RequestParam Long id){
        return ResponseEntity
                .ok()
                .body(productService.getProduct(id));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestParam Long id, @RequestBody ProductCreateDTO product){
        return ResponseEntity
                .ok()
                .body(productService.updateProduct(id, product));
    }




    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody ProductCreateDTO product){
            Product createdProduct = productService.addProduct(product);
            URI location = URI.create("/products/"+createdProduct.getId());
            return ResponseEntity
                    .created(location)
                    .location(location)
                    .body(createdProduct);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@RequestParam Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
