package com.example.CableERP.service;


import com.example.CableERP.entity.BillOfMaterials;
import com.example.CableERP.repository.BillOfMaterialsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillOfMaterialsService {

    private BillOfMaterialsRepository billOfMaterialsRepository;


    public List<BillOfMaterials> getBill(Long productId){
        return billOfMaterialsRepository.findAllByProductId(productId);
    }

    public BillOfMaterials addBill(BillOfMaterials billOfMaterials){
        return billOfMaterialsRepository.saveAndFlush(billOfMaterials);
    }


}
