package com.example.CableERP.Inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    Inventory findByComponentId(@Param("component_id") Long componentId);

}
