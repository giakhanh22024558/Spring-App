package com.example.demo.repository.prices;

import com.example.demo.model.prices.GroupPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupPriceRepository extends JpaRepository<GroupPrice, Long> {

    Optional<GroupPrice> findById(Long id);

    List<GroupPrice> findByGroupId(Long groupId);

    List<GroupPrice> findByPriceId(Long priceId);

    Optional<GroupPrice> findByGroupIdAndPriceId(Long groupId, Long priceId);
}
