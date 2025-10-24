package com.example.CableERP.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory_item")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "qty_available")
    private double qtyAvailable;

    @Column(name = "qty_reserved")
    private double qtyReserved;

    @OneToOne
    @JoinColumn(name="component_id",referencedColumnName = "id")
    private Component component;


    public Inventory(Long id, double qtyAvailable, double qtyReserved) {
        this.id = id;
        this.qtyAvailable = qtyAvailable;
        this.qtyReserved = qtyReserved;
    }

    protected Inventory(){}


    public Long getId() {
        return id;
    }

    public double getQtyAvailable() {
        return qtyAvailable;
    }

    public void setQtyAvailable(double qtyAvailable) {
        this.qtyAvailable = qtyAvailable;
    }

    public double getQtyReserved() {
        return qtyReserved;
    }

    public void setQtyReserved(double qtyReserved) {
        this.qtyReserved = qtyReserved;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }
}
