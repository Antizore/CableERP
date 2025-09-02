package com.example.CableERP.service;

import com.example.CableERP.entity.Customer;
import com.example.CableERP.exception.DuplicateEmailException;
import com.example.CableERP.repository.CustomerRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;





@ExtendWith(MockitoExtension.class)
class CustomerServiceUnitTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;


    @Test
    void testCreateCustomerDuplicateEmail() {
        Customer existing = new Customer(1L, "John", "123-443-221", "johndoe@gmail.com");
        Customer newCustomer = new Customer(null, "John2", "123-444-221", "johndoe@gmail.com");

        // Simulate existing customer
        when(customerRepository.findCustomerByEmail(newCustomer.getEmail())).thenReturn(existing);

        DuplicateEmailException exception = assertThrows(
                DuplicateEmailException.class,
                () -> customerService.createCustomer(newCustomer)
        );

        assertEquals("Customer with email johndoe@gmail.com already exists.", exception.getMessage());
        // Ensure save() is never called
        verify(customerRepository, never()).save(any());
    }
}

