package com.example.CableERP.BillOfMaterials;


import com.example.CableERP.Common.Exception.DuplicateException;
import com.example.CableERP.Common.Exception.MissingEntityException;
import com.example.CableERP.Common.Exception.WrongValueException;
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

        if(!product.getBillOfMaterialsList().isEmpty()) throw new DuplicateException("Bill of materials " +
                "exist for this product, please update or delete current BOM if you want to add new");

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


    /*
    Mój tok rozumowania jest taki, że front dostaje bom danego przedmiotu. Użytkownik sobie go edytuje i ja dostaję z
    powrotem w jsonie wynik tego jak teraz użytkownik chce aby wyglądał bom. Powiedzmy, że ma

    component_1 20szt
    component_2 33szt

    Dodał
    component_3 10szt

    oraz zmodyfikował
    component_1 20szt -> component_1 10szt

    Payload jaki oczekuję z powrotem to:

    component_1 10szt
    component_2 33szt
    component_3 10szt

    w teorii mógłbym wymagać tylko zmiany, ale nie spodziewam się w erpach że produkt składa się z miliona komponentów
    a tak jest mi wygodniej :)
     */
    public void updateBill(List<BomCreatingDTO> bomCreatingDTOList, Long id){
        Product product = productRepository.findById(id).orElseThrow();
        List<BillOfMaterials> productBOM = product.getBillOfMaterialsList();
        List<BillOfMaterials> map1 = new ArrayList<>();

        for(BomCreatingDTO item : bomCreatingDTOList){
            Component component = componentRepository.findById(item.componentId()).orElseThrow(() -> new MissingEntityException("Component not found"));
            if (item.qty() <= 0 ) throw new WrongValueException("Qty cannot be less or equal 0");
            map1.add(
                    new BillOfMaterials(
                            product,
                            component,
                            item.qty()
                    )
            );
        }

        List<Long> bomIds = productBOM.stream().mapToLong(BillOfMaterials::getId).boxed().toList();
        billOfMaterialsRepository.deleteAllByIdInBatch(bomIds);
        billOfMaterialsRepository.saveAllAndFlush(map1);
    }

    public void deleteBill(Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        List<BillOfMaterials> productBOM = product.getBillOfMaterialsList();
        List<Long> bomIds = productBOM.stream().mapToLong(BillOfMaterials::getId).boxed().toList();
        billOfMaterialsRepository.deleteAllByIdInBatch(bomIds);
    }


}

