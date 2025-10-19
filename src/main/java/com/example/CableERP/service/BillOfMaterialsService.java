package com.example.CableERP.service;


import com.example.CableERP.DTOs.BillOfMaterialsDTO;
import com.example.CableERP.DTOs.ComponentDTO;
import com.example.CableERP.entity.BillOfMaterials;
import com.example.CableERP.entity.Component;
import com.example.CableERP.entity.Product;
import com.example.CableERP.repository.BillOfMaterialsRepository;
import com.example.CableERP.repository.ComponentRepository;
import com.example.CableERP.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BillOfMaterialsService {

    private final BillOfMaterialsRepository billOfMaterialsRepository;

    public BillOfMaterialsService(BillOfMaterialsRepository billOfMaterialsRepository, ComponentRepository componentRepository, ProductRepository productRepository) {
        this.billOfMaterialsRepository = billOfMaterialsRepository;
    }




    public List<BillOfMaterialsDTO> getBill(Long productId){
        return billOfMaterialsRepository.findAllByProduct_Id(productId).stream().map(
                billOfMaterials -> new BillOfMaterialsDTO(
                        billOfMaterials.getQty(),
                        new ComponentDTO(
                                billOfMaterials.getComponent().getName(),
                                billOfMaterials.getComponent().getUnit().toString()
                        ))).collect(Collectors.toList());
    }

    private EntityManager entityManager;

    public void addBill(List<BillOfMaterials> billOfMaterialsList){

        for(BillOfMaterials bill : billOfMaterialsList){
            billOfMaterialsRepository.saveAndFlush(bill);
        }

    }


}
