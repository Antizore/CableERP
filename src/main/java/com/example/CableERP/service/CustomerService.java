package com.example.CableERP.service;

import com.example.CableERP.entity.Customer;
import com.example.CableERP.exception.DuplicateEmailException;
import com.example.CableERP.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;


    public Customer createCustomer(Customer customer) {
        Optional<Customer> existingCustomer = Optional.ofNullable(customerRepository.findCustomerByEmail(customer.getEmail()));
        if(existingCustomer.isEmpty()) return customerRepository.save(customer);
        else throw new DuplicateEmailException("Customer with email " + customer.getEmail() + "already exists.");
    }


}
