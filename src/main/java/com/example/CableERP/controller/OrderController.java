package com.example.CableERP.controller;


import com.example.CableERP.entity.CustomerOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {




    //TODO:
    @PostMapping
    public ResponseEntity<String> createNewOrder(){
        return ResponseEntity.ok().body("");
    }

    //TODO:
    @GetMapping
    public ResponseEntity<String> getAllOrders(){
        return ResponseEntity.ok().body("");
    }

    //TODO:
    @GetMapping("/{id}")
    public ResponseEntity<CustomerOrder> getSpecificOrder(@PathVariable Long id){
        return ResponseEntity.ok().build();
    }



}
