package com.example.CableERP.controller;


import com.example.CableERP.entity.CustomerOrder;
import com.example.CableERP.service.CustomerOrderService;
import com.example.CableERP.DTOs.CreateOrderDTO;
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


    @PostMapping
    public ResponseEntity<String> createNewOrder(@RequestBody CreateOrderDTO customerOrder){
        orderService.saveOrderToDB(customerOrder);
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
