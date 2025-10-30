package com.example.CableERP.controller;

import com.example.CableERP.DTOs.ReservingComponentDTO;
import com.example.CableERP.entity.Reservation;
import com.example.CableERP.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class ReservationController {


    final ReservationService reservationService;


    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }


    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getAllReservations(){
        return ResponseEntity
                .ok()
                .body(reservationService.getAllReservations());
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
    public void reserveComponent(@RequestBody ReservingComponentDTO reservation){
        reservationService.froze(reservation);
    }

    // to będzie response entity
    @PostMapping("/{id}/release")
    public void releaseComponent(@RequestBody ReservingComponentDTO reservation){
        reservationService.release(reservation);
    }






}
