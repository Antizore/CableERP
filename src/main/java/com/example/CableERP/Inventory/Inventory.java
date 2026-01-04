package com.example.CableERP.Inventory;

import com.example.CableERP.Component.Component;
import jakarta.persistence.*;

@Entity
@Table(name = "inventory_item")
public class Inventory {

    protected Inventory(){}


    public Inventory(double qtyAvailable, double qtyReserved, Component component) {
        this.qtyAvailable = qtyAvailable;
        this.qtyReserved = qtyReserved;
        this.component = component;
    }



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "qty_available")
    private double qtyAvailable;

    @Column(name = "qty_reserved")
    private double qtyReserved;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="component_id",referencedColumnName = "id")
    private Component component;


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
