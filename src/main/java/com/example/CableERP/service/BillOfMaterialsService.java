package com.example.CableERP.service;


import com.example.CableERP.DTOs.BillOfMaterialsDTO;
import com.example.CableERP.DTOs.ComponentDTO;
import com.example.CableERP.DTOs.BomCreatingDTO;
import com.example.CableERP.entity.BillOfMaterials;
import com.example.CableERP.repository.BillOfMaterialsRepository;
import com.example.CableERP.repository.ComponentRepository;
import com.example.CableERP.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
