package com.example.CableERP.entity;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<BillOfMaterials> billOfMaterialsList = new ArrayList<>();



    protected Product() {}


    public Product(Long id, String name, String description, List<BillOfMaterials> billOfMaterialsList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.billOfMaterialsList = billOfMaterialsList;
    }


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


