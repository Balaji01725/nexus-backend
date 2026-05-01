package com.nexus.service;

import com.nexus.model.Product;
import com.nexus.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllActive() {
        return productRepository.findByActiveTrueOrderByCreatedAtDesc();
    }

    public Product getById(String id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    public Product create(Product product, String adminEmail) {
        product.setAddedByEmail(adminEmail);
        return productRepository.save(product);
    }

    public Product update(String id, Product updated, String adminEmail) {
        Product existing = getById(id);
        existing.setTitle(updated.getTitle());
        existing.setPrice(updated.getPrice());
        existing.setBrowsingLink(updated.getBrowsingLink());
        existing.setDescription(updated.getDescription());
        return productRepository.save(existing);
    }

    public void softDelete(String id) {
        Product p = getById(id);
        p.setActive(false);
        productRepository.save(p);
    }

    public List<Product> searchByTitle(String title) {
        return productRepository.findByTitleContainingIgnoreCaseAndActiveTrue(title);
    }
}
