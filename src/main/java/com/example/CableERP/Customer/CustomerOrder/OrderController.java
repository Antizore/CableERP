package com.example.CableERP.Customer.CustomerOrder;


import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Transactional
@RequestMapping("/customers/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping
    public ResponseEntity<Order> createNewOrder(@RequestBody CreateOrderDTO customerOrder) {
        return ResponseEntity
                .ok()
                .body(orderService.saveOrderToDB(customerOrder));

    }


    @PostMapping("/{orderId}/items")
    public ResponseEntity<?> createItemsInOrder(@PathVariable Long orderId, @RequestBody List<CreateItemsInOrderDTO> itemsInOrderDTO) {
        orderService.addItemsToOrder(orderId, itemsInOrderDTO);
        return ResponseEntity
                .ok()
                .build();
    }


    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<ShowOrderDTO>> getAllOrders() {
        return ResponseEntity
                .ok()
                .body(orderService.returnAllOrders());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ShowOrderDTO> getSpecificOrder(@PathVariable Long id) {

        return ResponseEntity
                .ok()
                .body(orderService.returnOrderById(id));
    }


    @PatchMapping("/{orderId}/items")
    public ResponseEntity<?> updateItemInOrder(@PathVariable Long orderId, @RequestParam Long itemId, @RequestBody Double qty) {
        orderService.updateItemInOrder(orderId, itemId, qty);
        return ResponseEntity
                .ok()
                .build();
    }



    @DeleteMapping("/{orderId}/items")
    public ResponseEntity<?> deleteItemFromOrder(@PathVariable Long orderId, @RequestParam(required = false) Long itemId) {
        orderService.deleteItemsFromOrder(orderId,itemId);
        return ResponseEntity
                .ok()
                .build();
    }


}
