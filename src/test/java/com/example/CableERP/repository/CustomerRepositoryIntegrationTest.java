package com.example.CableERP.repository;

import com.example.CableERP.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class CustomerRepositoryIntegrationTest {

    @Autowired
    private  CustomerRepository customerRepository;




    @Test
    void testSaveAndFindCustomerByEmail(){
        Customer customer = new Customer("John","123-443-221","johndoe@gmail.com");
        customerRepository.save(customer);
        assertThat(customerRepository.findCustomerByEmail("johndoe@gmail.com")).isNotNull();
    }
    

}
