package com.example.demo.service;

import com.example.demo.model.products.Product;
import com.example.demo.model.products.ProductCategory;
import com.example.demo.model.products.ProductUnit;
import com.example.demo.repository.products.ProductRepository;
import com.example.demo.repository.products.ProductCategoryRepository;
import com.example.demo.repository.products.ProductUnitRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductUnitRepository productUnitRepository;

    public ProductService(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository, ProductUnitRepository productUnitRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productUnitRepository = productUnitRepository;
    }

    // ==========================
    // CRUD for PRODUCTS
    // ==========================
    public ResponseEntity<?> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    public ResponseEntity<?> getProductById(Integer id) {
        try {
            Optional<Product> product = productRepository.findById(id);
            return product.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(404).body((Product) createError("Product not found")));
        } catch (Exception e) {
            return handleException(e);
        }
    }

    public ResponseEntity<?> saveProduct(Product product) {
        try {
            return ResponseEntity.ok(productRepository.save(product));
        } catch (Exception e) {
            return handleException(e);
        }
    }

    public ResponseEntity<?> deleteProduct(Integer id) {
        try {
            if (!productRepository.existsById(id)) {
                return ResponseEntity.status(404).body(createError("Product not found"));
            }
            productRepository.deleteById(id);
            return ResponseEntity.ok(createMessage("Product deleted successfully"));
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // ==========================
    // CRUD for PRODUCT CATEGORIES
    // ==========================
    public ResponseEntity<?> getAllCategories() {
        try {
            return ResponseEntity.ok(productCategoryRepository.findAll());
        } catch (Exception e) {
            return handleException(e);
        }
    }

    public ResponseEntity<?> getCategoryById(Integer id) {
        try {
            Optional<ProductCategory> category = productCategoryRepository.findById(id);
            return category.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(404).body((ProductCategory) createError("Category not found")));
        } catch (Exception e) {
            return handleException(e);
        }
    }

    public ResponseEntity<?> saveCategory(ProductCategory category) {
        try {
            return ResponseEntity.ok(productCategoryRepository.save(category));
        } catch (Exception e) {
            return handleException(e);
        }
    }

    public ResponseEntity<?> deleteCategory(Integer id) {
        try {
            if (!productCategoryRepository.existsById(id)) {
                return ResponseEntity.status(404).body(createError("Category not found"));
            }
            productCategoryRepository.deleteById(id);
            return ResponseEntity.ok(createMessage("Category deleted successfully"));
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // ==========================
    // CRUD for PRODUCT UNITS
    // ==========================
    public ResponseEntity<?> getAllUnits() {
        try {
            return ResponseEntity.ok(productUnitRepository.findAll());
        } catch (Exception e) {
            return handleException(e);
        }
    }

    public ResponseEntity<?> getUnitById(Integer id) {
        try {
            Optional<ProductUnit> unit = productUnitRepository.findById(id);
            return unit.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(404).body((ProductUnit) createError("Unit not found")));
        } catch (Exception e) {
            return handleException(e);
        }
    }

    public ResponseEntity<?> saveUnit(ProductUnit unit) {
        try {
            return ResponseEntity.ok(productUnitRepository.save(unit));
        } catch (Exception e) {
            return handleException(e);
        }
    }

    public ResponseEntity<?> deleteUnit(Integer id) {
        try {
            if (!productUnitRepository.existsById(id)) {
                return ResponseEntity.status(404).body(createError("Unit not found"));
            }
            productUnitRepository.deleteById(id);
            return ResponseEntity.ok(createMessage("Unit deleted successfully"));
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // ==========================
    // Utility Methods for Error Handling
    // ==========================
    private ResponseEntity<Map<String, String>> handleException(Exception e) {
        return ResponseEntity.status(500).body(createError("Internal Server Error: " + e.getMessage()));
    }

    private Map<String, String> createError(String message) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        return errorResponse;
    }

    private Map<String, String> createMessage(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return response;
    }
}
