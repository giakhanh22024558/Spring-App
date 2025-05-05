package com.example.demo.controller;

import com.example.demo.model.stockandwarehouse.StockAndWarehouse;
import com.example.demo.service.StockAndWarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "keycloak")
@RestController
@RequestMapping("/store/api")
public class StockAndWarehouseController {

    private final StockAndWarehouseService stockAndWarehouseService;

    public StockAndWarehouseController(StockAndWarehouseService stockAndWarehouseService) {
        this.stockAndWarehouseService = stockAndWarehouseService;
    }

    @GetMapping("/stock-and-warehouse")
    public ResponseEntity<?> getAllStockAndWarehouse() {
        return stockAndWarehouseService.getAllStockAndWarehouse();
    }

    @GetMapping("/stock-and-warehouse/{id}")
    public ResponseEntity<?> getStockAndWarehouseById(@PathVariable Integer id) {
        return stockAndWarehouseService.getStockAndWarehouseById(id);
    }

    @PostMapping("/stock-and-warehouse")
    public ResponseEntity<?> createStockAndWarehouse(@RequestBody StockAndWarehouse stockAndWarehouse) {
        return stockAndWarehouseService.saveStockAndWarehouse(stockAndWarehouse);
    }

    @PutMapping("/stock-and-warehouse/{id}")
    public ResponseEntity<?> updateStockAndWarehouse(@PathVariable Integer id, @RequestBody StockAndWarehouse stockAndWarehouse) {
        stockAndWarehouse.setId(id);
        return stockAndWarehouseService.saveStockAndWarehouse(stockAndWarehouse);
    }

    @DeleteMapping("/stock-and-warehouse/{id}")
    public ResponseEntity<?> deleteStockAndWarehouse(@PathVariable Integer id) {
        return stockAndWarehouseService.deleteStockAndWarehouse(id);
    }
}