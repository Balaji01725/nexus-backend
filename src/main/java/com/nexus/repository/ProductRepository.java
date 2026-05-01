package com.nexus.repository;

import com.nexus.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByActiveTrueOrderByCreatedAtDesc();
    List<Product> findByTitleContainingIgnoreCaseAndActiveTrue(String title);
}
