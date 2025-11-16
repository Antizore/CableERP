package com.example.CableERP.Order;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }


    @PostMapping
    public ResponseEntity<String> createNewOrder(@RequestBody CreateOrderDTO customerOrder){
        orderService.saveOrderToDB(customerOrder);
        return ResponseEntity.ok().body("");

    }



    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(){
        return ResponseEntity
                .ok()
                .body(orderService.returnAllOrders());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Order> getSpecificOrder(@PathVariable Long id){

        return ResponseEntity
                .ok()
                .body(orderService.returnOrderById(id));
    }



}
