package com.example.CableERP.entity;


import com.example.CableERP.enums.Unit;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "component")
public class Component {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    private Double costPerUnit;

    @OneToMany(mappedBy = "component")
    private List<BillOfMaterials> billOfMaterialsList = new ArrayList<>();

    protected Component(){}


    public Component(Long id, String name, Unit unit, Double costPerUnit, List<BillOfMaterials> billOfMaterialsList) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.costPerUnit = costPerUnit;
        this.billOfMaterialsList = billOfMaterialsList;
    }


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


}
