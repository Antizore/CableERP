package com.example.CableERP.controller;


import com.example.CableERP.entity.Customer;
import com.example.CableERP.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;


    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer){
        Customer createdCustomer = customerService.createCustomer(customer);
        URI location = URI.create("/customers/"+createdCustomer.getName());
        return ResponseEntity
                .created(location)
                .body(createdCustomer);
    }



}
