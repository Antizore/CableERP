package com.example.CableERP.service;


import com.example.CableERP.DTOs.ReservingComponentDTO;
import com.example.CableERP.entity.Component;
import com.example.CableERP.entity.Inventory;
import com.example.CableERP.entity.Reservation;
import com.example.CableERP.enums.Status;
import com.example.CableERP.repository.ComponentRepository;
import com.example.CableERP.repository.InventoryRepository;
import com.example.CableERP.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@Service
public class ReservationService {

    final ReservationRepository reservationRepository;
    final ComponentRepository componentRepository;
    final InventoryRepository inventoryRepository;

    public ReservationService(ReservationRepository reservationRepository, ComponentRepository componentRepository, InventoryRepository inventoryRepository){
        this.reservationRepository = reservationRepository;
        this.componentRepository = componentRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public List<Reservation> getAllReservations (){
        return reservationRepository.findAll();
    }




    // TODO: trzeba połączyć to z inventory

    public void froze(ReservingComponentDTO reservingComponentDTO){
        Inventory inventory = inventoryRepository.findByComponentId(reservingComponentDTO.componentId());
        if(inventory.getQtyAvailable() < inventory.getQtyReserved() + reservingComponentDTO.qty()){
        }
        else {
            inventory.setQtyReserved(inventory.getQtyReserved() + reservingComponentDTO.qty());
            Component component = componentRepository.findById(reservingComponentDTO.componentId()).orElse(null);


            Calendar rightNow = Calendar.getInstance();
            Reservation reservation = new Reservation(1L,component, reservingComponentDTO.qty(), Status.FROZEN,new Timestamp(rightNow.getTimeInMillis()));
            inventoryRepository.saveAndFlush(inventory);
            reservationRepository.saveAndFlush(reservation);

        }
    }



    public void release(){

    }


}
