package com.example.CableERP.repository;

import com.example.CableERP.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

interface ProductRepository extends JpaRepository<Product, Long> {
}
