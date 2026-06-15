package simpleerp.customer;

import simpleerp.common.exception.DuplicateException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Transactional
    public Customer createCustomer(CreateCustomerDTO customer) {
        Optional<Customer> existingCustomer = Optional.ofNullable(
                customerRepository.findCustomerByEmail(customer.email()));
        if (existingCustomer.isPresent()) throw new
                DuplicateException("Customer with email " + customer.email() + " already exists.");
        else return customerRepository.saveAndFlush(new Customer(customer.name(),customer.phone(), customer.email()));
    }


    public List<Customer> getListOfCustomers(){
        return customerRepository.findAll();
    }




    }



