package com.example.CableERP.controller;


import com.example.CableERP.entity.Customer;
import com.example.CableERP.service.CustomerService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/customers")

public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getCustomers(){
        return ResponseEntity
                .ok()
                .body(customerService.getListOfCustomers());
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer){
        Customer createdCustomer = customerService.createCustomer(customer);
        URI location = URI.create("/customers/"+createdCustomer.getName());
        return ResponseEntity
                .created(location)
                .body(createdCustomer);
    }
}
