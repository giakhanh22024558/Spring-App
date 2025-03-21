package com.example.demo.controller;

import com.example.demo.model.products.Product;
import com.example.demo.model.products.ProductCategory;
import com.example.demo.model.products.ProductUnit;
import com.example.demo.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/greeting")
    public ResponseEntity<?> greeting() {
        return ResponseEntity.ok("Hello, this is from products");
    }

    // ==========================
    // CRUD for PRODUCTS
    // ==========================
    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id) {
        return productService.getProductById(id);
    }

    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        product.setId(id);
        return productService.saveProduct(product);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        return productService.deleteProduct(id);
    }

    // ==========================
    // CRUD for PRODUCT CATEGORIES
    // ==========================
    @GetMapping("/products/categories")
    public ResponseEntity<?> getAllCategories() {
        return productService.getAllCategories();
    }

    @GetMapping("/products/categories/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Integer id) {
        return productService.getCategoryById(id);
    }

    @PostMapping("/products/categories")
    public ResponseEntity<?> createCategory(@RequestBody ProductCategory category) {
        return productService.saveCategory(category);
    }

    @PutMapping("/products/categories/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer id, @RequestBody ProductCategory category) {
        category.setId(id);
        return productService.saveCategory(category);
    }

    @DeleteMapping("/products/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
        return productService.deleteCategory(id);
    }

    // ==========================
    // CRUD for PRODUCT UNITS
    // ==========================
    @GetMapping("/products/units")
    public ResponseEntity<?> getAllUnits() {
        return productService.getAllUnits();
    }

    @GetMapping("/products/units/{id}")
    public ResponseEntity<?> getUnitById(@PathVariable Integer id) {
        return productService.getUnitById(id);
    }

    @PostMapping("/products/units")
    public ResponseEntity<?> createUnit(@RequestBody ProductUnit unit) {
        return productService.saveUnit(unit);
    }

    @PutMapping("/products/units/{id}")
    public ResponseEntity<?> updateUnit(@PathVariable Integer id, @RequestBody ProductUnit unit) {
        unit.setId(id);
        return productService.saveUnit(unit);
    }

    @DeleteMapping("/products/units/{id}")
    public ResponseEntity<?> deleteUnit(@PathVariable Integer id) {
        return productService.deleteUnit(id);
    }
}
