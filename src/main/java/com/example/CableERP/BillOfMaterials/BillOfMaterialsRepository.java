package com.example.CableERP.BillOfMaterials;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillOfMaterialsRepository extends JpaRepository<BillOfMaterials, Long> {



    List<BillOfMaterials> findAllByProduct_Id(@Param("product_id") Long productId);
    List<BillOfMaterials> findAllByComponent_Id(@Param("component_id") Long componentId);
    List<BillOfMaterials> findAllByProduct_Name(String name);


}
