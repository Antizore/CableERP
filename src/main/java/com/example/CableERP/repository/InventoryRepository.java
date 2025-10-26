package com.example.CableERP.repository;

import com.example.CableERP.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    Inventory findByComponentId(@Param("component_id") Long componentId);

}
