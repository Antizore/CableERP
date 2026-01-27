package com.example.CableERP.Customer.CustomerOrder;


import com.example.CableERP.MRP.Estimation;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@RestController
@Transactional
@RequestMapping("/customers/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }



    // TODO: add estimated days in response
    @PostMapping
    public ResponseEntity<Order> createNewOrder(@RequestParam Long customerId, @RequestBody List<CreateItemsInOrderDTO> itemsInOrderDTO) {
        Order order = orderService.saveOrderToDB(itemsInOrderDTO,customerId);
        return ResponseEntity
                .ok()
                .body(order);
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


    /*
    Patching can break optimization algorithm, for now comment
    @PatchMapping("/{orderId}/items")
    public ResponseEntity<?> updateItemInOrder(@PathVariable Long orderId, @RequestParam Long itemId, @RequestBody Double qty) {
        orderService.updateItemInOrder(orderId, itemId, qty);
        return ResponseEntity
                .ok()
                .build();
    }

     */

    @DeleteMapping("/{orderId}/items")
    public ResponseEntity<?> deleteItemFromOrder(@PathVariable Long orderId, @RequestParam(required = false) Long itemId) {
        orderService.deleteItemsFromOrder(orderId,itemId);
        return ResponseEntity
                .ok()
                .build();
    }


}
