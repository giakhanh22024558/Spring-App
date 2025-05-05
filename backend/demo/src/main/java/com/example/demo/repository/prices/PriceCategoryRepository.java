package com.example.demo.repository.prices;

import com.example.demo.model.prices.PriceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PriceCategoryRepository extends JpaRepository<PriceCategory, Long> {

    Optional<PriceCategory> findById(Long id);

    List<PriceCategory> findByNameContainingIgnoreCase(String name);
}
