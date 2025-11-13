package com.example.CableERP.controller;


import com.example.CableERP.entity.CustomerOrder;
import com.example.CableERP.service.CustomerOrderService;
import org.junit.jupiter.api.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CustomerOrderService orderService;

    public OrderController(CustomerOrderService orderService){
        this.orderService = orderService;
    }


    //TODO:
    @PostMapping
    public ResponseEntity<String> createNewOrder(){
        return ResponseEntity.ok().body("");
    }



    @GetMapping
    public ResponseEntity<List<CustomerOrder>> getAllOrders(){
        return ResponseEntity
                .ok()
                .body(orderService.returnAllOrders());
    }


    @GetMapping("/{id}")
    public ResponseEntity<CustomerOrder> getSpecificOrder(@PathVariable Long id){

        return ResponseEntity
                .ok()
                .body(orderService.returnOrderById(id));
    }



}
