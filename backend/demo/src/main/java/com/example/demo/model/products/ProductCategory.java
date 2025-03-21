package com.example.demo.model.products;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "products_categories")
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private LocalDateTime createdTime;
    private Boolean inUsed;

    @PrePersist
    protected void onCreate() {
        this.createdTime = LocalDateTime.now();
    }

}
