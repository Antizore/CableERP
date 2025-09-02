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


/*

!!!!!!!!!!!!!!
@SpringBootTest
@Transactional
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    CustomerServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCustomerDuplicateEmail() {
        Customer customer1 = new Customer(null, "John", "123-443-221", "johndoe@gmail.com");
        customerService.createCustomer(customer1);

        // This fails because customerRepository is a mock, so findCustomerByEmail always returns null
        assertThat(customerRepository.findCustomerByEmail("johndoe@gmail.com")).isNotNull();

        Customer customer2 = new Customer(null, "John2", "123-444-221", "johndoe@gmail.com");
        DuplicateEmailException exception = assertThrows(
                DuplicateEmailException.class,
                () -> customerService.createCustomer(customer2)
        );
        assertEquals("Customer with email johndoe@gmail.com already exists.", exception.getMessage());
    }

    @Test
    void testCreateCustomer() {
        Customer customer = new Customer(null, "John", "123-443-221", "johndoe@gmail.com");
        customerService.createCustomer(customer);

        // This also fails for the same reason
        assertThat(customerRepository.findCustomerByEmail("johndoe@gmail.com")).isNotNull();
    }
}
!!!!!!!!!!!!!




6️⃣ Takeaways

Mocks do not persist data. If you call findCustomerByEmail() on a mock without stubbing, it will always return null.

@SpringBootTest + mocks = confusing hybrid. Either mock everything (unit test) or use Spring beans (integration test).
 Mixing both is a common source of subtle test failures.

Test order affecting results usually comes from shared state in the database or incorrect mock setup.


 */



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

