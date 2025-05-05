package com.example.demo.service;

import com.example.demo.model.stockandwarehouse.StockAndWarehouse;
import com.example.demo.repository.stockandwarehouse.StockAndWarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockAndWarehouseService {

    @Autowired
    private StockAndWarehouseRepository stockAndWarehouseRepository;

    public ResponseEntity<?> getAllStockAndWarehouse() {
        List<StockAndWarehouse> stockAndWarehouses = stockAndWarehouseRepository.findAll();
        return ResponseEntity.ok(stockAndWarehouses);
    }

    public ResponseEntity<?> getStockAndWarehouseById(Integer id) {
        Optional<StockAndWarehouse> stockAndWarehouse = stockAndWarehouseRepository.findById(id);
        return stockAndWarehouse.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> saveStockAndWarehouse(StockAndWarehouse stockAndWarehouse) {
        StockAndWarehouse savedStockAndWarehouse = stockAndWarehouseRepository.save(stockAndWarehouse);
        return ResponseEntity.ok(savedStockAndWarehouse);
    }

    public ResponseEntity<?> deleteStockAndWarehouse(Integer id) {
        if (stockAndWarehouseRepository.existsById(id)) {
            stockAndWarehouseRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}