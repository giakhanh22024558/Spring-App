package com.example.demo.controller;

import com.example.demo.dto.PriceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.PriceService;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    private final PriceService priceService;

    @Autowired
    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping
    public ResponseEntity<Page<Map<String, Object>>> getAllPrices(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            Pageable pageable) {
        Page<Map<String, Object>> prices = priceService.findAll(name, categoryId, date, pageable);
        return ResponseEntity.ok(prices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPriceById(@PathVariable Long id) {
        Map<String, Object> price = priceService.findById(id);
        return ResponseEntity.ok(price);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPrice(@Valid @RequestBody PriceRequest priceRequest) {
        Map<String, Object> newPrice = priceService.create(priceRequest);
        return new ResponseEntity<>(newPrice, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePrice(
            @PathVariable Long id,
            @Valid @RequestBody PriceRequest priceRequest) {
        Map<String, Object> updatedPrice = priceService.update(id, priceRequest);
        return ResponseEntity.ok(updatedPrice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrice(@PathVariable Long id) {
        priceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<Map<String, Object>>> getActivePrices() {
        List<Map<String, Object>> activePrices = priceService.findActivePrices();
        return ResponseEntity.ok(activePrices);
    }

    @GetMapping("/default")
    public ResponseEntity<Map<String, Object>> getDefaultPrice() {
        Map<String, Object> defaultPrice = priceService.findDefaultPrice();
        return ResponseEntity.ok(defaultPrice);
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<List<Map<String, Object>>> getPricesByCategory(@PathVariable Long categoryId) {
        List<Map<String, Object>> prices = priceService.findByCategory(categoryId);
        return ResponseEntity.ok(prices);
    }

    @GetMapping("/products/{productCode}")
    public ResponseEntity<List<Map<String, Object>>> getPricesByProduct(@PathVariable String productCode) {
        List<Map<String, Object>> prices = priceService.findByProduct(productCode);
        return ResponseEntity.ok(prices);
    }

    @GetMapping("/groups/{groupId}")
    public ResponseEntity<List<Map<String, Object>>> getPricesByGroup(@PathVariable Long groupId) {
        List<Map<String, Object>> prices = priceService.findByGroup(groupId);
        return ResponseEntity.ok(prices);
    }
}
