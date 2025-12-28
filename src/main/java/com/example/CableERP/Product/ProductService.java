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

    public ProductDTO updateProduct(Long id, ProductCreateDTO product){
        Product productToBeUpdated = productRepository.findById(id).orElseThrow();
        if(!(product.name() == null || product.name().isBlank())) productToBeUpdated.setName(product.name());
        if(!(product.description() == null || product.description().isBlank())) productToBeUpdated.setDescription(product.description());
        // yeah I know, but just wanted it to work for now otherwise i will never end this first project
        productRepository.saveAndFlush(productToBeUpdated);
        return getProduct(productToBeUpdated.getId());
    }


    public Product addProduct(ProductCreateDTO product) {
        if(product.name() == null || product.name().isBlank()) throw new NoNameException("Cannot add product without name");
        else {
            return productRepository.saveAndFlush(new Product(product.name(), product.description()));
        }
    }


}
