package com.example.CableERP.service;


import com.example.CableERP.entity.BillOfMaterials;
import com.example.CableERP.entity.Component;
import com.example.CableERP.entity.Product;
import com.example.CableERP.repository.BillOfMaterialsRepository;
import com.example.CableERP.repository.ComponentRepository;
import com.example.CableERP.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillOfMaterialsService {

    private final BillOfMaterialsRepository billOfMaterialsRepository;
    private final ComponentRepository componentRepository;
    private final ProductRepository productRepository;

    public BillOfMaterialsService(BillOfMaterialsRepository billOfMaterialsRepository, ComponentRepository componentRepository, ProductRepository productRepository) {
        this.billOfMaterialsRepository = billOfMaterialsRepository;
        this.componentRepository = componentRepository;
        this.productRepository = productRepository;
    }




    public List<BillOfMaterials> getBill(Long productId){
        return billOfMaterialsRepository.findAllByProduct_Id(productId);
    }

    public void addBill(List<BillOfMaterials> billOfMaterialsList){


        for(var bill : billOfMaterialsList){
            Optional<Product> product = productRepository.findById(bill.getProduct().getId());
            Optional<Component> component = componentRepository.findById(bill.getComponent().getId());

            BillOfMaterials billToSave = new BillOfMaterials(null, product.get(), component.get(), bill.getQty());

            billOfMaterialsRepository.saveAndFlush(bill);
        }

    }


}
