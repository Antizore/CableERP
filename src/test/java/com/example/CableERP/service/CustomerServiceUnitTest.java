package com.example.CableERP.service;

import com.example.CableERP.entity.Customer;
import com.example.CableERP.exception.DuplicateException;
import com.example.CableERP.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;





@ExtendWith(MockitoExtension.class)
class CustomerServiceUnitTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void getListOfCustomersShouldReturnCustomersFromRepository(){
        Customer customer1 = new Customer(1L, "John", "123-443-221", "johndoe@gmail.com");
        Customer customer2 = new Customer(1L, "John", "123-443-221", "johndoe@gmail.com");
        List<Customer> customerList = List.of(customer1,customer2);

        when(customerRepository.findAll()).thenReturn(customerList);
        List<Customer> result = customerService.getListOfCustomers();

        assertEquals(2, result.size());
        assertTrue(result.contains(customer1));
        assertTrue(result.contains(customer2));
        verify(customerRepository,times(1)).findAll();

    }

    @Test
    void getListOfCustomersShouldReturnEmptyIfNoCustomers(){

        when(customerRepository.findAll()).thenReturn(List.of());

        List<Customer> result = customerService.getListOfCustomers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(customerRepository,times(1)).findAll();
    }


    @Test
    void testCreateCustomerDuplicateEmail() {
        Customer existing = new Customer(1L, "John", "123-443-221", "johndoe@gmail.com");
        Customer newCustomer = new Customer(null, "John2", "123-444-221", "johndoe@gmail.com");

        // Simulate existing customer
        when(customerRepository.findCustomerByEmail(newCustomer.getEmail())).thenReturn(existing);

        DuplicateException exception = assertThrows(
                DuplicateException.class,
                () -> customerService.createCustomer(newCustomer)
        );

        assertEquals("Customer with email johndoe@gmail.com already exists.", exception.getMessage());
        // Ensure save() is never called
        verify(customerRepository, never()).save(any());
    }
}

