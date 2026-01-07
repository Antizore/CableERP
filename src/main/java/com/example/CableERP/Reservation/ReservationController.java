package com.example.CableERP.Reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class ReservationController {


    final ReservationService reservationService;


    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity
                .ok()
                .body(reservationService.getAllReservations());
    }


    @PostMapping("/reserve")
    public ResponseEntity<?> reserveComponent(@RequestBody ReservingComponentDTO reservation) {
        reservationService.froze(reservation);
        return ResponseEntity
                .ok()
                .build();
    }


    @PostMapping("/release")
    public ResponseEntity<?> releaseComponent(@RequestBody ReservingComponentDTO reservation) {
        reservationService.release(reservation);
        return ResponseEntity
                .ok()
                .build();
    }


}
