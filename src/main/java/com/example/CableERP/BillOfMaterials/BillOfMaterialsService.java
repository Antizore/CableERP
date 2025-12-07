package com.example.CableERP.BillOfMaterials;


import com.example.CableERP.Component.Component;
import com.example.CableERP.Component.ComponentDTO;
import com.example.CableERP.Component.ComponentRepository;
import com.example.CableERP.Product.Product;
import com.example.CableERP.Product.ProductDTO;
import com.example.CableERP.Product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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


    /*
    TODO: NIE MOGĄ BYĆ DWA TAKIE SAME KOMPONENTY DODANE DLA TEGO SAMEGO PRODUKTU, CZYLI NP NIE MOŻE BYĆ TAK, ŻE DWA
     RAZY ZOSTANIE DODANY WTYK USB-C
     */
    public void addBill(List<BomCreatingDTO> billOfMaterialsList, Long id){

        for(BomCreatingDTO bill : billOfMaterialsList){

            BillOfMaterials bill1 = new BillOfMaterials(
                    productRepository.findById(id).get(),
                    componentRepository.findById(id).get(),
                    bill.qty()
            );

            billOfMaterialsRepository.saveAndFlush(bill1);
        }

    }


}
