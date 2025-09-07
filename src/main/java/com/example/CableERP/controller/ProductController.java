package com.example.CableERP.controller;


import com.example.CableERP.entity.Product;
import com.example.CableERP.service.ProductService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(name = "/products")
public class ProductController {

    private ProductService productService;


    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        // TODO: MAKE PAGEABLE
        return ResponseEntity
                    .ok()
                    .body(productService.getAllProducts());
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        Product createdProduct = productService.addProduct(product);
        URI location = URI.create("/products/"+product.getName());
        return ResponseEntity
                .created(location)
                .body(createdProduct);
    }

}
