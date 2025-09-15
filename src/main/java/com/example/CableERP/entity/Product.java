package com.example.CableERP.entity;


import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "product")
public record Product(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id,
        String name,
        String description,
        @OneToMany(mappedBy = "product")
        List<BillOfMaterials> billOfMaterialsList
) { }







