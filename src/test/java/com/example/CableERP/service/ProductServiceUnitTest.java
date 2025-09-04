package com.example.CableERP.service;


import com.example.CableERP.entity.Product;
import com.example.CableERP.exception.NoNameException;
import com.example.CableERP.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ProductServiceUnitTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;


    @Test
    void blankNameShouldThrowNoNameException(){

        Product product = new Product(1L,"","Some description");
        Product product1 = new Product(2L,"   ","Some description");
        Product product2 = new Product(3L,null,"Some description");

        NoNameException exception = assertThrows(NoNameException.class, () -> productService.addProduct(product));
        NoNameException exception1 = assertThrows(NoNameException.class, () -> productService.addProduct(product1));
        NoNameException exception2 = assertThrows(NoNameException.class, () -> productService.addProduct(product2));
        assertEquals("Cannot add product without name", exception.getMessage());
        assertEquals("Cannot add product without name", exception1.getMessage());
        assertEquals("Cannot add product without name", exception2.getMessage());

    }



}
