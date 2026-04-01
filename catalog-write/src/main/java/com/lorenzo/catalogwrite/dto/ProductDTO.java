package com.lorenzo.catalogwrite.dto;

import java.math.BigDecimal;

public record ProductDTO (
        Long id,
        String name,
        BigDecimal price
){}
