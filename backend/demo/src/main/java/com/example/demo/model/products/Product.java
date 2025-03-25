package com.example.demo.model.products;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime createdTime;
    private String NAME;
    private String nameText;
    private LocalDateTime updatedTime;
    private String description;
    private Boolean isDeleted;
    private Boolean inUsed;
    private String convertProductCode;
    private Double convertQuantity;
    private Double minQuantity;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_CATEGORY_ID", referencedColumnName = "id")
    private ProductCategory productCategory;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_UNIT_ID", referencedColumnName = "id")
    private ProductUnit productUnit;

    // Auto set createdTime when inserting
    @PrePersist
    protected void onCreate() {
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }

    // Auto update updatedTime when updating
    @PreUpdate
    protected void onUpdate() {
        this.updatedTime = LocalDateTime.now();
    }

}
