package com.example.CableERP.service;


import com.example.CableERP.Product.Product;
import com.example.CableERP.Product.ProductService;
import com.example.CableERP.exception.NoNameException;
import com.example.CableERP.Product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProductServiceUnitTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void getAllProductsShouldReturnListOfProduct(){

        Product product1 = new Product("Product 1", "Description of product 1");
        Product product2 = new Product("Product 2", "Description of product 2");
        List<Product> productList = List.of(product1,product2);

        //when(productRepository.findAll()).thenReturn(productList);
        //List<Product> result = productService.getAllProducts();

        //assertNotNull(result);
        //assertEquals(productList,result);

    }

    @Test
    void getAllProductsShouldReturnEmptyListIfNoProductsInDatabase(){

        when(productRepository.findAll()).thenReturn(List.of());
        //List<Product> productList = productService.getAllProducts();
        //assertNotNull(productList);
        //assertTrue(productList.isEmpty());
        verify(productRepository, times(1)).findAll();

    }



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
