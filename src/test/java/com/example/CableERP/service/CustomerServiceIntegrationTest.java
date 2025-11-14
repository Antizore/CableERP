package com.example.CableERP.service;


import com.example.CableERP.entity.Customer;
import com.example.CableERP.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CustomerServiceIntegrationTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testCreateCustomerSuccess(){
        Customer customer = new Customer( "John", "123-456-789","johndoe@gmail.com");
        customerService.createCustomer(customer);
        assertThat(customerRepository.findCustomerByEmail(customer.getEmail())).isNotNull();
    }


}
