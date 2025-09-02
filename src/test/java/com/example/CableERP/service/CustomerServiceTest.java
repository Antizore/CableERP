package com.example.CableERP.service;

import com.example.CableERP.entity.Customer;
import com.example.CableERP.exception.DuplicateEmailException;
import com.example.CableERP.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    CustomerServiceTest(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateCustomerDuplicateEmail(){
        Customer customer1 = new Customer(null, "John", "123-443-221","johndoe@gmail.com");
        Customer customer2 = new Customer(null, "John", "123-443-221","johndoe@gmail.com");
        customerService.createCustomer(customer1);
        DuplicateEmailException exception = assertThrows(
                DuplicateEmailException.class,
                () -> customerService.createCustomer(customer2)
        );
        assertEquals("Customer with email johndoe@gmail.com already exists.", exception.getMessage());
    }



}
