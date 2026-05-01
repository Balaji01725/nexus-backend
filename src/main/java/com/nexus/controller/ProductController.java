package com.nexus.controller;

import com.nexus.model.Product;
import com.nexus.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ── PUBLIC ──────────────────────────────────────────────

    @GetMapping("/api/products")
    public ResponseEntity<List<Product>> getAll(
        @RequestParam(required = false) String search
    ) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(productService.searchByTitle(search));
        }
        return ResponseEntity.ok(productService.getAllActive());
    }

    @GetMapping("/api/products/{id}")
    public ResponseEntity<Product> getOne(@PathVariable String id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    // ── ADMIN ────────────────────────────────────────────────

    @PostMapping("/api/admin/products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> create(
        @Valid @RequestBody Product product,
        Principal principal
    ) {
        Product saved = productService.create(product, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/api/admin/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> update(
        @PathVariable String id,
        @Valid @RequestBody Product product,
        Principal principal
    ) {
        return ResponseEntity.ok(productService.update(id, product, principal.getName()));
    }

    @DeleteMapping("/api/admin/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String id) {
        productService.softDelete(id);
        return ResponseEntity.ok(Map.of("message", "Product deleted successfully"));
    }
}
