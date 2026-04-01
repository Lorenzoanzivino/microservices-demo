package com.lorenzo.catalogwrite.controller;

import com.lorenzo.catalogwrite.dto.ProductDTO;
import com.lorenzo.catalogwrite.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@RequestBody ProductDTO productDTO){
        return productService.save(productDTO);
    }

    @GetMapping(value = "/{id}" , produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO findById(@PathVariable Long id){
        return productService.findById(id);
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDTO> findAll(){
        return productService.findAll();
    }

    @PutMapping(value ="/{id}", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO update(@PathVariable Long id, @RequestBody ProductDTO productDTO){
        return productService.updateProduct(id, productDTO);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        productService.delete(id);
    }
}
