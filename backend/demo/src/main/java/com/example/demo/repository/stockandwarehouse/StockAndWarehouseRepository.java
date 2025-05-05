package com.example.demo.repository.stockandwarehouse;

import com.example.demo.model.stockandwarehouse.StockAndWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockAndWarehouseRepository extends JpaRepository<StockAndWarehouse, Integer> {
}