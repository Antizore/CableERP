package com.example.CableERP.Procurement;

import com.example.CableERP.Component.Component;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.sql.Time;

@Entity
@Table(name = "procurement")
public class Procurement {

    protected Procurement(){}

    public Procurement(Long id, String name, String phone, String email, Double leadTimeDays, Time createdAt) {
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
    @OneToOne
    @JoinColumn(name = "component_id")
    private Component component;


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

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }
}
