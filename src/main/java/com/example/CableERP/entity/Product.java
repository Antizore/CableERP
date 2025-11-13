package com.example.CableERP.entity;


import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    protected Product() {}


    public Product(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<BillOfMaterials> billOfMaterialsList = new ArrayList<>();




    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<BillOfMaterials> getBillOfMaterialsList() { return billOfMaterialsList; }
    public void setBillOfMaterialsList(List<BillOfMaterials> billOfMaterialsList) {
        this.billOfMaterialsList = billOfMaterialsList;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", billOfMaterialsList=" + billOfMaterialsList +
                '}';
    }
}


