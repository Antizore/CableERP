package com.example.CableERP.repository;


import com.example.CableERP.entity.BillOfMaterials;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillOfMaterialsRepository extends JpaRepository<BillOfMaterials, Long> {



    List<BillOfMaterials> findAllByProduct_Id(@Param("product_id") Long productId);

}
