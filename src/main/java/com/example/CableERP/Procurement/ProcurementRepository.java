package com.example.CableERP.Procurement;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcurementRepository extends JpaRepository<Procurement, Long> {


    @Query
    boolean existsByName(String name);

}
