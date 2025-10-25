package com.example.CableERP.controller;

import com.example.CableERP.entity.Reservation;
import com.example.CableERP.service.ReservationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("orders")
public class ReservationController {


    final ReservationService reservationService;


    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
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
  "reservationId": 10, -> tabela stock_reservation
  "status": "FROZEN", -> tabela stock_reservation
  "availableAfterReservation": 130 -> tabela inventory_item
}


     */

    @PostMapping("/{id}/reserve")
    public void reserveComponent(@RequestBody Reservation reservation, @PathVariable Long id){

    }

    // to będzie response entity
    @PostMapping("/{id}/release")
    public void releaseComponent(@RequestBody Reservation reservation, @PathVariable Long id){

    }






}
