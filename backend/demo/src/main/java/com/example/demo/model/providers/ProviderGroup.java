package com.example.demo.model.providers;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "provider_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @OneToMany(mappedBy = "providerGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Provider> providers = new ArrayList<>();
}
