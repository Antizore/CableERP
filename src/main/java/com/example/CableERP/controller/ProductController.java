package com.example.CableERP.controller;


import com.example.CableERP.entity.Product;
import com.example.CableERP.service.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
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
