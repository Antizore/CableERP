package com.example.CableERP.Vendor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Time;

@Entity
public class Vendor {

    protected Vendor(){}

    public Vendor(Long id, String name, String phone, String email, Double leadTimeDays, Time createdAt) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.leadTimeDays = leadTimeDays;
        this.createdAt = createdAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
    private String email;
    private Double leadTimeDays;
    private Time createdAt;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getLeadTimeDays() {
        return leadTimeDays;
    }

    public void setLeadTimeDays(Double leadTimeDays) {
        this.leadTimeDays = leadTimeDays;
    }

    public Time getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Time createdAt) {
        this.createdAt = createdAt;
    }
}
