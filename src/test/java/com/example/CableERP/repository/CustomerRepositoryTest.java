package com.example.CableERP.repository;

import com.example.CableERP.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private  CustomerRepository customerRepository;

    /*

    WARNING: Dynamic loading of agents will be disallowed by default in a future release
Java HotSpot(TM) 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
Hibernate: select c1_0.id,c1_0.email,c1_0.name,c1_0.phone from customer c1_0 where c1_0.id=?
Hibernate: select c1_0.id,c1_0.email,c1_0.name,c1_0.phone from customer c1_0 where c1_0.id=?

org.springframework.orm.ObjectOptimisticLockingFailureException: Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect): [com.example.CableERP.entity.Customer#1]

    This typically happens in Spring Data JPA tests when:

You are setting the id manually (1L) while saving a new entity.

By default, JPA expects the id to be generated (via @GeneratedValue).

If you pass an explicit 1L, Hibernate may think this entity already exists → it issues an UPDATE instead of an INSERT.

Since no row with id=1 exists in the in-memory DB, the update affects 0 rows → Hibernate throws ObjectOptimisticLockingFailureException.


     */


    @Test
    void testSaveAndFindCustomerByEmail(){
        Customer customer = new Customer(null,"John","123-443-221","johndoe@gmail.com");
        customerRepository.save(customer);
        assertThat(customerRepository.findCustomerByEmail("johndoe@gmail.com")).isNotNull();
    }
    

}
