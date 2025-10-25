package com.example.CableERP.entity;

import com.example.CableERP.enums.Status;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "stock_reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="component_id",referencedColumnName = "id")
    private Component component;

    private double qty;

    private Status status;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

}


/*
 id BIGSERIAL PRIMARY KEY,
    order_id BIGINT, -- pojawi się w Faza 3
    component_id BIGINT NOT NULL REFERENCES component(id) ON DELETE CASCADE,
    qty NUMERIC(10,2) NOT NULL CHECK (qty > 0),
    status VARCHAR(20) NOT NULL CHECK (status IN ('FROZEN', 'RELEASED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP


 */