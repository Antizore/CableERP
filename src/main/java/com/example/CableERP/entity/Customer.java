package com.example.CableERP.entity;

import jakarta.persistence.*;



@Entity
@Table(name = "customer")
public class Customer{

    protected Customer(){};

    public Customer(Long id, String name, String phone, String email){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email=email;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
    @Column(name="email")
    private String email;

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
}
