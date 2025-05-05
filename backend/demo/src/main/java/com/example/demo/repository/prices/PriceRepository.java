package com.example.demo.repository.prices;

import com.example.demo.model.prices.Price;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

    Optional<Price> findById(Long id);

    List<Price> findByIsDefaultTrue();

    List<Price> findByEndDateAfter(LocalDateTime date);

    List<Price> findByStartDateBeforeAndEndDateAfter(LocalDateTime start, LocalDateTime end);

    // Thêm phương thức phân trang cho startDate và endDate
    Page<Price> findByStartDateBeforeAndEndDateAfter(LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Price> findByPriceCategoryId(Long categoryId);

    List<Price> findByNameContainingIgnoreCase(String name);

    // Thêm các phương thức hỗ trợ phân trang
    Page<Price> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Price> findByPriceCategoryId(Long categoryId, Pageable pageable);

    Page<Price> findByNameContainingIgnoreCaseAndPriceCategoryId(String name, Long categoryId, Pageable pageable);
}