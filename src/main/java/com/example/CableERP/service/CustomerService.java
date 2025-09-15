package com.example.CableERP.service;

import com.example.CableERP.entity.Customer;
import com.example.CableERP.exception.DuplicateException;
import com.example.CableERP.exception.NoEmailException;
import com.example.CableERP.repository.CustomerRepository;
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

    public List<Customer> getListOfCustomers(){
        return customerRepository.findAll();
    }

    @Transactional
    public Customer createCustomer(Customer customer) {
        Optional<Customer> existingCustomer = Optional.ofNullable(customerRepository.findCustomerByEmail(customer.getEmail()));
        if(existingCustomer.isEmpty() && !customer.getEmail().isEmpty())  return customerRepository.saveAndFlush(customer);
        else if (customer.getEmail().isEmpty()) throw new NoEmailException("Cannot add Customer without email");
        else throw new DuplicateException("Customer with email " + customer.getEmail() + " already exists.");
    }


}
