package com.example.CableERP.Product;


import com.example.CableERP.BillOfMaterials.BillOfMaterialsDTO;
import com.example.CableERP.Common.Exception.MissingEntityException;
import com.example.CableERP.Component.ComponentDTO;
import com.example.CableERP.Common.Exception.NoNameException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public List<ProductDTO> getAllProducts(){



        List<Product> products = productRepository.findAll();

        return products.stream().map(product -> new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getBillOfMaterialsList().stream()
                        .map(bom -> new BillOfMaterialsDTO(
                                bom.getQty(),
                                new ComponentDTO(
                                        bom.getComponent().getName(),
                                        bom.getComponent().getUnit().toString()
                                )
                        ))
                        .collect(Collectors.toList())
        )).collect(Collectors.toList());

    }


    public ProductDTO getProduct(Long id){
        return productRepository.findById(id)
                .map(
                        product -> new ProductDTO(
                                product.getId(),
                                product.getName(),
                                product.getDescription(),
                                product.getBillOfMaterialsList().stream().map(
                                        bom -> new BillOfMaterialsDTO(
                                                bom.getQty(),
                                                new ComponentDTO(
                                                        bom.getComponent().getName(),
                                                        bom.getComponent().getUnit().toString()
                                                )
                                        )
                                ).collect(Collectors.toList())
                        )
                ).orElseThrow();
    }

    public ProductDTO updateProduct(Long id, Product product){

        Product productToBeUpdated = productRepository.findById(id).orElseThrow();
        if(!(product.getName() == null || product.getName().isBlank())) productToBeUpdated.setName(product.getName());
        if(!(product.getDescription() == null || product.getDescription().isBlank())) productToBeUpdated.setDescription(product.getDescription());
        productRepository.saveAndFlush(productToBeUpdated);

        return getProduct(id);

    }


    public Product addProduct(Product product) {
        if(product.getName() == null || product.getName().isBlank()) throw new NoNameException("Cannot add product without name");
        else {
            return productRepository.saveAndFlush(product);
        }
    }


}
