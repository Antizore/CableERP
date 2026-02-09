package com.example.SimpleERP.Reservation;

import com.example.SimpleERP.Component.Component;
import com.example.SimpleERP.Component.ComponentRepository;
import com.example.SimpleERP.Customer.CustomerOrder.Order;
import com.example.SimpleERP.Customer.CustomerOrder.OrderRepository;
import com.example.SimpleERP.Customer.CustomerOrder.OrderService;
import com.example.SimpleERP.Inventory.Inventory;
import com.example.SimpleERP.Inventory.InventoryRepository;
import com.example.SimpleERP.Common.Exception.WrongValueException;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ComponentRepository componentRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public ReservationService(ReservationRepository reservationRepository,
                              ComponentRepository componentRepository,
                              InventoryRepository inventoryRepository,
                              OrderRepository orderRepository,
                              @Lazy OrderService orderService) {
        this.reservationRepository = reservationRepository;
        this.componentRepository = componentRepository;
        this.inventoryRepository = inventoryRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
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
        inventoryRepository.saveAndFlush(inventory);
        reservationRepository.saveAndFlush(reservation);
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        Inventory inventory = reservation.getComponent().getInventory();
        double newReservedQty = inventory.getQtyReserved() - reservation.getQty();
        inventory.setQtyReserved(Math.max(newReservedQty, 0));
        inventoryRepository.save(inventory);
        reservationRepository.delete(reservation);
    }

    public double checkFreeStock(Long componentId) {
        return inventoryRepository.findByComponentId(componentId)
                .map(inv -> inv.getQtyAvailable() - inv.getQtyReserved())
                .orElse(0.0);
    }

    @Transactional
    public void reallocateStockForComponent(Long componentId) {
        Inventory inventory = inventoryRepository.findByComponentId(componentId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        double availableToGive = inventory.getQtyAvailable();
        List<Reservation> allReservations = reservationRepository
                .findAllByComponentIdOrderByCustomerOrderCreatedAtAsc(componentId);

        for (Reservation reservation : allReservations) {
            double needed = reservation.getQty();

            if (availableToGive >= needed) {
                if (!reservation.isFulfilled()) {
                    reservation.setFulfilled(true);
                    reservationRepository.saveAndFlush(reservation);
                    orderService.tryPromoteOrderToReady(reservation.getCustomerOrder().getId());
                }
                availableToGive -= needed;
            } else {
                if (reservation.isFulfilled()) {
                    reservation.setFulfilled(false);
                    reservationRepository.saveAndFlush(reservation);
                }
            }
        }
    }

}