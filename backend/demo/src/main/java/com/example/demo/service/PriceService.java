package com.example.demo.service;

import com.example.demo.dto.PriceRequest;
import com.example.demo.model.prices.GroupPrice;
import com.example.demo.model.prices.Price;
import com.example.demo.model.prices.PriceCategory;
import com.example.demo.model.prices.ProductPrice;
import com.example.demo.repository.prices.GroupPriceRepository;
import com.example.demo.repository.prices.PriceCategoryRepository;
import com.example.demo.repository.prices.PriceRepository;
import com.example.demo.repository.prices.ProductPriceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PriceService {

    private final PriceRepository priceRepository;
    private final PriceCategoryRepository priceCategoryRepository;
    private final ProductPriceRepository productPriceRepository;
    private final GroupPriceRepository groupPriceRepository;

    @Autowired
    public PriceService(
            PriceRepository priceRepository,
            PriceCategoryRepository priceCategoryRepository,
            ProductPriceRepository productPriceRepository,
            GroupPriceRepository groupPriceRepository) {
        this.priceRepository = priceRepository;
        this.priceCategoryRepository = priceCategoryRepository;
        this.productPriceRepository = productPriceRepository;
        this.groupPriceRepository = groupPriceRepository;
    }

    public Page<Map<String, Object>> findAll(String name, Long categoryId, LocalDateTime date, Pageable pageable) {
        Page<Price> prices;

        if (name != null && categoryId != null) {
            prices = priceRepository.findByNameContainingIgnoreCaseAndPriceCategoryId(name, categoryId, pageable);
        } else if (name != null) {
            prices = priceRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if (categoryId != null) {
            prices = priceRepository.findByPriceCategoryId(categoryId, pageable);
        } else if (date != null) {
            prices = priceRepository.findByStartDateBeforeAndEndDateAfter(date, date, pageable);
        } else {
            prices = priceRepository.findAll(pageable);
        }

        return prices.map(this::convertToResponse);
    }

    public Map<String, Object> findById(Long id) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Price not found with id: " + id));
        return convertToResponse(price);
    }

    @Transactional
    public Map<String, Object> create(PriceRequest priceRequest) {
        Price price = convertToEntity(priceRequest);
        price.setCreatedTime(LocalDateTime.now());
        price.setUpdatedTime(LocalDateTime.now());

        // If this is set as default, unset other default prices
        if (Boolean.TRUE.equals(price.getIsDefault())) {
            Price finalPrice = price;
            priceRepository.findByIsDefaultTrue().forEach(p -> {
                if (!p.getId().equals(finalPrice.getId())) {
                    p.setIsDefault(false);
                    priceRepository.save(p);
                }
            });
        }

        price = priceRepository.save(price);
        return convertToResponse(price);
    }

    @Transactional
    public Map<String, Object> update(Long id, PriceRequest priceRequest) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Price not found with id: " + id));

        updatePriceFromRequest(price, priceRequest);
        price.setUpdatedTime(LocalDateTime.now());

        // If this is set as default, unset other default prices
        if (Boolean.TRUE.equals(price.getIsDefault())) {
            Price finalPrice = price;
            priceRepository.findByIsDefaultTrue().forEach(p -> {
                if (!p.getId().equals(finalPrice.getId())) {
                    p.setIsDefault(false);
                    priceRepository.save(p);
                }
            });
        }

        price = priceRepository.save(price);
        return convertToResponse(price);
    }

    @Transactional
    public void delete(Long id) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Price not found with id: " + id));

        // Check if this price is used by product prices or group prices
        if (!productPriceRepository.findByPriceId(id).isEmpty() ||
                !groupPriceRepository.findByPriceId(id).isEmpty()) {
            throw new IllegalStateException("Cannot delete price that is in use");
        }

        priceRepository.delete(price);
    }

    public List<Map<String, Object>> findActivePrices() {
        LocalDateTime now = LocalDateTime.now();
        List<Price> activePrices = priceRepository.findByStartDateBeforeAndEndDateAfter(now, now);
        return activePrices.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Map<String, Object> findDefaultPrice() {
        Price defaultPrice = priceRepository.findByIsDefaultTrue()
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No default price found"));
        return convertToResponse(defaultPrice);
    }

    public List<Map<String, Object>> findByCategory(Long categoryId) {
        List<Price> prices = priceRepository.findByPriceCategoryId(categoryId);
        return prices.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> findByProduct(String productCode) {
        List<ProductPrice> productPrices = productPriceRepository.findByProductCode(productCode);
        return productPrices.stream()
                .map(ProductPrice::getPrice)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> findByGroup(Long groupId) {
        List<GroupPrice> groupPrices = groupPriceRepository.findByGroupId(groupId);
        return groupPrices.stream()
                .map(GroupPrice::getPrice)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private Price convertToEntity(PriceRequest priceRequest) {
        Price price = new Price();
        updatePriceFromRequest(price, priceRequest);
        return price;
    }

    private void updatePriceFromRequest(Price price, PriceRequest request) {
        price.setName(request.getName());
        price.setNameText(request.getNameText());
        price.setStartDate(request.getStartDate());
        price.setEndDate(request.getEndDate());
        price.setIsDefault(request.getIsDefault() != null ? request.getIsDefault() : false);

        if (request.getPriceCategoryId() != null) {
            PriceCategory category = priceCategoryRepository.findById(request.getPriceCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Price Category not found with id: " + request.getPriceCategoryId()));
            price.setPriceCategory(category);
        }
    }

    private Map<String, Object> convertToResponse(Price price) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", price.getId());
        response.put("name", price.getName());
        response.put("nameText", price.getNameText());
        response.put("startDate", price.getStartDate());
        response.put("endDate", price.getEndDate());
        response.put("isDefault", price.getIsDefault());
        response.put("createdTime", price.getCreatedTime());
        response.put("updatedTime", price.getUpdatedTime());

        // Calculate if price is active
        LocalDateTime now = LocalDateTime.now();
        boolean isActive = (price.getStartDate() == null || price.getStartDate().isBefore(now)) &&
                (price.getEndDate() == null || price.getEndDate().isAfter(now));
        response.put("isActive", isActive);

        if (price.getPriceCategory() != null) {
            response.put("priceCategoryId", price.getPriceCategory().getId());
            response.put("priceCategoryName", price.getPriceCategory().getName());
        }

        return response;
    }
}