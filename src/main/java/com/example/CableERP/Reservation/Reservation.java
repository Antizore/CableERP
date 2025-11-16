package com.example.CableERP.Reservation;

import com.example.CableERP.Component.Component;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "stock_reservation")
public class Reservation {

    protected Reservation(){}

    public Reservation(Long id, Component component, double qty, ReservationStatus status, Timestamp createdAt){
        this.component = component;
        this.qty = qty;
        this.status = status;
        this.createdAt = createdAt;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="component_id",referencedColumnName = "id")
    private Component component;

    @Column(name = "qty")
    private double qty;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setReservationStatus(ReservationStatus status) {
        this.status = status;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Long getId() {
        return id;
    }

    public Component getComponent() {
        return component;
    }

    public double getQty() {
        return qty;
    }

    public ReservationStatus getReservationStatus() {
        return status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}


/*
 id BIGSERIAL PRIMARY KEY,
    order_id BIGINT, -- pojawi się w Faza 3
    component_id BIGINT NOT NULL REFERENCES component(id) ON DELETE CASCADE,
    qty NUMERIC(10,2) NOT NULL CHECK (qty > 0),
    status VARCHAR(20) NOT NULL CHECK (status IN ('FROZEN', 'RELEASED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP


 */