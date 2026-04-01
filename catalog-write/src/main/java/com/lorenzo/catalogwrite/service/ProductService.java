package com.lorenzo.catalogwrite.service;

import com.lorenzo.catalogwrite.dto.ProductDTO;
import com.lorenzo.catalogwrite.entity.Product;
import com.lorenzo.catalogwrite.mapper.ProductMapper;
import com.lorenzo.catalogwrite.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    // Ricevere prodottoDTO dall'esterno e convertirlo in Entity

    public ProductDTO save(ProductDTO productDTO) {
        // 1. Validazione
        if (productDTO.name() == null || productDTO.price().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Errore: campi non compilati o prezzo negativo");
        }

        if (productRepository.findByName(productDTO.name()) != null) {
            throw new RuntimeException(("Errore, prodotto con nome " + productDTO.name()) + " già esistente");
        }

        // 2. Mappatura da DTO a Entity (passiamo solo la variabile productDTO)
        // qui ID è NULL
        Product product = productMapper.toEntity(productDTO);

        // 3. Salvataggio nel Database
        Product savedProduct = productRepository.save(product);

        // 4. Ritorno del DTO mappato dall'Entity salvata
        // qui ID ha il valore proprio
        return productMapper.toDto(savedProduct);
    }

    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        return productMapper.toDto(product);
    }

    public List<ProductDTO> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        // 1. Cerchiamo il prodotto esistente nel database 🔍
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato con ID: " + id));

        // 1.1 Verifico che il nome non sia già occupato da un ALTRO prodotto (ID diverso)
        Product duplicate = productRepository.findByName(productDTO.name());
        if (duplicate != null && !duplicate.getId().equals(id)) {
            throw new RuntimeException("Errore: il nome è già usato da un altro prodotto");
        }
        // 2. Aggiorniamo i campi dell'entità trovata con i nuovi dati ✏️
        existingProduct.setName(productDTO.name());
        existingProduct.setPrice(productDTO.price());
        // 3. Salviamo le modifiche (JPA capirà che è un UPDATE e non un INSERT) 💾
        Product updatedProduct = productRepository.save(existingProduct);
        // 4. Convertiamo l'entità aggiornata in DTO e la restituiamo 🔄
        return productMapper.toDto(updatedProduct);
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Errore, Prodotto non trovato" + id);
        }
        productRepository.deleteById(id);
    }
}
