package com.example.CableERP.controller;

import com.example.CableERP.entity.Orders;
import com.example.CableERP.service.ComponentService;
import com.example.CableERP.service.OrdersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("orders")
public class OrdersController {


    final OrdersService ordersService;


    public OrdersController(OrdersService ordersService){
        this.ordersService = ordersService;
    }



    // to będzie response entity

    /*
    IN
      {
      "componentId": 1,
      "qty": 20
      }
    OUT
    {
  "reservationId": 10,
  "status": "FROZEN",
  "availableAfterReservation": 130
}


     */

    @PostMapping("/{id}/reserve")
    public void reserveComponent(@RequestBody Orders order,@PathVariable Long id){

    }

    // to będzie response entity
    @PostMapping("/{id}/release")
    public void releaseComponent(@RequestBody Orders orders, @PathVariable Long id){

    }






}
