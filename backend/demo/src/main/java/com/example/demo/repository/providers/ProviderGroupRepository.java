package com.example.demo.repository.providers;

import com.example.demo.model.providers.ProviderGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderGroupRepository extends JpaRepository<ProviderGroup, Long> {

    Optional<ProviderGroup> findById(Long id);

    List<ProviderGroup> findByNameContainingIgnoreCase(String name);

    List<ProviderGroup> findByIsDeletedFalse();

    boolean existsByNameIgnoreCase(String name);
}
