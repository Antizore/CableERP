package com.example.CableERP.controller;


import com.example.CableERP.entity.Product;
import com.example.CableERP.service.ProductService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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



    //TODO naprawić że jak w jsonie dasz pole name to go nie przyjmuje
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
            Product createdProduct = productService.addProduct(product);
            URI location = URI.create("/products/"+product.getName());
            return ResponseEntity
                    .created(location)
                    .location(location)
                    .body(createdProduct);
    }

}
