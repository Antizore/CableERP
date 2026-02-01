package com.example.CableERP.Reservation;

import com.example.CableERP.Component.Component;
import com.example.CableERP.Component.ComponentRepository;
import com.example.CableERP.Customer.CustomerOrder.Order;
import com.example.CableERP.Customer.CustomerOrder.OrderRepository;
import com.example.CableERP.Inventory.Inventory;
import com.example.CableERP.Inventory.InventoryRepository;
import com.example.CableERP.Common.Exception.WrongValueException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ComponentRepository componentRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderRepository orderRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ComponentRepository componentRepository,
                              InventoryRepository inventoryRepository,
                              OrderRepository orderRepository) {
        this.reservationRepository = reservationRepository;
        this.componentRepository = componentRepository;
        this.inventoryRepository = inventoryRepository;
        this.orderRepository = orderRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }


    @Transactional
    public void makeReservation(ReservationRequestDTO request) {
        if (request.qty() <= 0) {
            throw new WrongValueException("Quantity must be greater than 0");
        }
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new RuntimeException("Order not found: " + request.orderId()));
        Component component = componentRepository.findById(request.componentId())
                .orElseThrow(() -> new RuntimeException("Component not found: " + request.componentId()));
        Inventory inventory = component.getInventory();
        if (inventory == null) {
            inventory = new Inventory(component, 0, 0);
            component.setInventory(inventory);
        }
        inventory.setQtyReserved(inventory.getQtyReserved() + request.qty());
        Reservation reservation = new Reservation(order, component, request.qty());
        boolean hasCoverage = inventory.getQtyAvailable() >= inventory.getQtyReserved();
        reservation.setFulfilled(hasCoverage);
        inventoryRepository.save(inventory);
        reservationRepository.save(reservation);
    }


    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        Inventory inventory = reservation.getComponent().getInventory();
        double newReservedQty = inventory.getQtyReserved() - reservation.getQty();
        inventory.setQtyReserved(Math.max(newReservedQty, 0));
        inventoryRepository.save(inventory);
        reservationRepository.delete(reservation); // Usuwamy rekord rezerwacji
    }

    public double checkFreeStock(Long componentId) {
        try {
            Inventory inv = inventoryRepository.findByComponentId(componentId);
            return inv.getQtyAvailable() - inv.getQtyReserved();
        } catch (Exception e) {
            return 0.0;
        }
    }
}