package com.example.CableERP.service;


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

    private EntityManager entityManager;

    public void addBill(List<BillOfMaterials> billOfMaterialsList){


        for(BillOfMaterials bill : billOfMaterialsList){

            Long pid = bill.getProduct().getId();
            Long cid = bill.getComponent().getId();
            Product p = productRepository.findById(pid).orElse(null);
            Component c = componentRepository.findById(cid).orElse(null);

            Product product = entityManager.getReference(Product.class, bill.getProduct().getId());
            Component component = entityManager.getReference(Component.class, bill.getComponent().getId());



            System.out.println("incoming product id = " + pid + ", product from repo = " + p);
            System.out.println("incoming component id = " + cid + ", component from repo = " + c);

            System.out.println("entityManager.contains(product) = " + (p != null && entityManager.contains(p)));
            System.out.println("entityManager.contains(component) = " + (c != null && entityManager.contains(c)));

            bill.setProduct(product);
            bill.setComponent(component);

            billOfMaterialsRepository.saveAndFlush(bill);
        }

    }


}
