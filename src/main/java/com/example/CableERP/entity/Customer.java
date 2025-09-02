package com.example.CableERP.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class Customer{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name, phone,email;

    public Customer(Long id, String name, String phone, String email) {
        this.id=id;
        this.name=name;
        this.phone=phone;
        this.email=email;
    }
}
