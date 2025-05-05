package com.example.demo.repository.prices;

import com.example.demo.model.prices.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {

    Optional<ProductPrice> findById(Long id);

    List<ProductPrice> findByProductCode(String productCode);

    List<ProductPrice> findByPriceId(Long priceId);

    Optional<ProductPrice> findByProductCodeAndPriceId(String productCode, Long priceId);

    List<ProductPrice> findByInUsedTrue();
}
