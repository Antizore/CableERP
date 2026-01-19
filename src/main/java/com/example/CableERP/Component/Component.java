package com.example.CableERP.Component;


import com.example.CableERP.BillOfMaterials.BillOfMaterials;
import com.example.CableERP.Inventory.Inventory;
import com.example.CableERP.Procurement.Procurement;
import com.example.CableERP.Reservation.Reservation;
import com.example.CableERP.Common.Unit;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "component")
public class Component {

    protected Component(){}
    public Component(String name, Unit unit, Double costPerUnit) {
        this.name = name;
        this.unit = unit;
        this.costPerUnit = costPerUnit;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    private Double costPerUnit;

    @OneToMany(mappedBy = "component")
    private List<BillOfMaterials> billOfMaterialsList = new ArrayList<>();

    @OneToOne(mappedBy = "component")
    private Inventory inventory;

    @OneToMany(mappedBy = "component")
    private List<Reservation> reservationList;

    @OneToOne(mappedBy = "component")
    private Procurement procurement;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Double getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(Double costPerUnit) {
        this.costPerUnit = costPerUnit;
    }

    public List<BillOfMaterials> getBillOfMaterialsList() {
        return billOfMaterialsList;
    }

    public void setBillOfMaterialsList(List<BillOfMaterials> billOfMaterialsList) {
        this.billOfMaterialsList = billOfMaterialsList;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public Procurement getProcurement() {
        return procurement;
    }

    public void setProcurement(Procurement procurement) {
        this.procurement = procurement;
    }
}
