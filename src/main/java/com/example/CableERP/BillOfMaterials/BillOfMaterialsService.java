package com.example.CableERP.BillOfMaterials;


import com.example.CableERP.Component.Component;
import com.example.CableERP.Component.ComponentDTO;
import com.example.CableERP.Component.ComponentRepository;
import com.example.CableERP.Product.Product;
import com.example.CableERP.Product.ProductDTO;
import com.example.CableERP.Product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BillOfMaterialsService {

    private final BillOfMaterialsRepository billOfMaterialsRepository;
    private final ProductRepository productRepository;
    private final ComponentRepository componentRepository;

    public BillOfMaterialsService(BillOfMaterialsRepository billOfMaterialsRepository, ComponentRepository componentRepository, ProductRepository productRepository) {
        this.billOfMaterialsRepository = billOfMaterialsRepository;
        this.productRepository = productRepository;
        this.componentRepository = componentRepository;
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

    public List<BillOfMaterialsDTO> getBill(Long componentId, Component component){
        return billOfMaterialsRepository.findAllByComponent_Id(componentId).stream().map(
                billOfMaterials -> new BillOfMaterialsDTO(
                        billOfMaterials.getQty(),
                        new ComponentDTO(
                                billOfMaterials.getComponent().getName(),
                                billOfMaterials.getComponent().getUnit().toString()
                        ))).collect(Collectors.toList());
    }



    public void addBill(List<BomCreatingDTO> billOfMaterialsList, Long id){
        Product product = productRepository.findById(id).orElseThrow();
        HashMap<Long,BillOfMaterials> bomToSend = new HashMap<>();

        for(BomCreatingDTO bill : billOfMaterialsList){
            Component component = componentRepository.findById(id).orElseThrow();

            BillOfMaterials billToPut = new BillOfMaterials(
                    product,
                    component,
                    bill.qty());

            bomToSend.put(component.getId(),billToPut);

            bomToSend.computeIfAbsent(
                    component.getId(), (v) -> billToPut);

            bomToSend.computeIfPresent(
                    component.getId(), (k,v) -> new BillOfMaterials(
                            product,
                            component,
                            bill.qty() + v.getQty()
                    )
            );
        }
        billOfMaterialsRepository.saveAllAndFlush(bomToSend.values().stream().toList());
    }


}
