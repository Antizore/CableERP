package com.example.CableERP.Order;


import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Transactional
@RequestMapping("/orders")
public class OrderController {
    
    private final OrderService orderService;
    
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }


    @PostMapping
    public ResponseEntity<Order> createNewOrder(@RequestBody CreateOrderDTO customerOrder){
        return ResponseEntity
                .ok()
                .body(orderService.saveOrderToDB(customerOrder));

    }

    /*
    TODO: 3.2 Dodanie pozycji do zamówienia

    Given zamówienie w statusie NEW
    When wysyłam POST /orders/{id}/items
    Then pozycja zostaje zapisana, a suma zamówienia się aktualizuje

     */
    @PostMapping("/{id}/items")
    public ResponseEntity<?> createItemsInOrder(@RequestParam Long orderId,@RequestBody List<CreateItemsInOrderDTO> itemsInOrderDTO){
        orderService.addItemsToOrder(orderId, itemsInOrderDTO);
        return ResponseEntity
                .ok()
                .build();
    }


    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<ShowOrderDTO>> getAllOrders(){
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
