package com.lorenzo.catalogwrite.mapper;

import com.lorenzo.catalogwrite.dto.ProductDTO;
import com.lorenzo.catalogwrite.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    // Trasforma l'Entity che arriva dal DB in un record DTO per il Controller
    public ProductDTO toDto(Product entity){
    if(entity == null) {
        return null;
    }
    return new ProductDTO(
            entity.getId(),
            entity.getName(),
            entity.getPrice()
        );
    }

    // Trasforma il DTO che arriva dal Controller in una Entity per il DB
    public Product toEntity(ProductDTO dto){
        if(dto == null){
            return null;
        }
        Product entity = new Product(dto.name(), dto.price());
        return entity;
    }
}
