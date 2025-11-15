package com.example.CableERP.Reservations;


import com.example.CableERP.Components.Component;
import com.example.CableERP.Inventory.Inventory;
import com.example.CableERP.enums.Status;
import com.example.CableERP.Components.ComponentRepository;
import com.example.CableERP.Inventory.InventoryRepository;
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
            inventory.setQtyAvailable(inventory.getQtyAvailable() - reservingComponentDTO.qty());
            Component component = componentRepository.findById(reservingComponentDTO.componentId()).orElse(null);
            Calendar rightNow = Calendar.getInstance();
            Reservation reservation = new Reservation(null,component, reservingComponentDTO.qty(), Status.FROZEN,new Timestamp(rightNow.getTimeInMillis()));
            inventoryRepository.saveAndFlush(inventory);
            reservationRepository.saveAndFlush(reservation);

        }
    }



    public void release(ReservingComponentDTO releasingComponentDTO){
        Inventory inventory = inventoryRepository.findByComponentId(releasingComponentDTO.componentId());
        if(inventory.getQtyReserved() < releasingComponentDTO.qty()){
        }
        else {
            inventory.setQtyAvailable(inventory.getQtyAvailable() + releasingComponentDTO.qty());
            inventory.setQtyReserved(inventory.getQtyReserved() - releasingComponentDTO.qty());
            Component component = componentRepository.findById(releasingComponentDTO.componentId()).orElse(null);
            Calendar rightNow = Calendar.getInstance();
            Reservation releasing = new Reservation(null, component, releasingComponentDTO.qty(), Status.RELEASED,new Timestamp(rightNow.getTimeInMillis()));
            inventoryRepository.saveAndFlush(inventory);
            reservationRepository.saveAndFlush(releasing);
        }
    }

    public void updateStatus(PatchReservationStatusDTO patchReservationStatusDTO, Long id){
        Reservation reservation = reservationRepository.findById(id).orElse(null);
        reservation.setStatus(patchReservationStatusDTO.status());
        reservationRepository.saveAndFlush(reservation);
    }


}
