package com.example.CableERP.controller;


import com.example.CableERP.entity.Product;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(name = "/products")
public class ProductController {

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){

        // TODO

        return (ResponseEntity<List<Product>>) List.of(null);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product){

        // TODO
        return ResponseEntity.ok().build();
    }

}
