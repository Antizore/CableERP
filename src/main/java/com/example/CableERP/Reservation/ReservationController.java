package com.example.CableERP.Reservation;

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
    public ResponseEntity reserveComponent(@RequestBody ReservingComponentDTO reservation){
        reservationService.froze(reservation);
        return ResponseEntity
                .ok()
                .build();
    }


    @PostMapping("/{id}/release")
    public ResponseEntity releaseComponent(@RequestBody ReservingComponentDTO reservation){
        reservationService.release(reservation);
        return ResponseEntity
                .ok()
                .build();
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity updateStatus(@PathVariable Long id, @RequestBody PatchReservationStatusDTO patchReservationStatusDTO)
    {
        reservationService.updateStatus(patchReservationStatusDTO, id);
        return ResponseEntity
                .ok()
                .build();
    }





}
