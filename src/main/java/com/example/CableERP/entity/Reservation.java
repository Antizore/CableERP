package com.example.CableERP.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "stock_reservation")
public class Reservation {

    private Long id;
    private Component component;

}


/*
 id BIGSERIAL PRIMARY KEY,
    order_id BIGINT, -- pojawi się w Faza 3
    component_id BIGINT NOT NULL REFERENCES component(id) ON DELETE CASCADE,
    qty NUMERIC(10,2) NOT NULL CHECK (qty > 0),
    status VARCHAR(20) NOT NULL CHECK (status IN ('FROZEN', 'RELEASED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP


 */