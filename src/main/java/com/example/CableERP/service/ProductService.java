package com.example.CableERP.service;


import com.example.CableERP.entity.Product;
import com.example.CableERP.exception.NoNameException;
import com.example.CableERP.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }


    public Product addProduct(Product product) {
        if(product.getName() == null || product.getName().isBlank()) throw new NoNameException("Cannot add product without name");
        else {
            return productRepository.saveAndFlush(product);
        }
    }


}
