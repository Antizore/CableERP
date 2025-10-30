package com.example.CableERP.entity;


import com.example.CableERP.enums.Status;
import jakarta.persistence.Entity;

import java.sql.Time;
import java.sql.Timestamp;

@Entity
public class CustomerOrder {

    private Long id;
    private Customer customer;
    private String orderNumber;
    private Status status;
    private Timestamp createdAt;
    private Time updatedAt;


}
