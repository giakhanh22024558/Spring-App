package com.example.demo.repository.customers;

import com.example.demo.model.customers.CustomerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerGroupRepository extends JpaRepository<CustomerGroup, Long> {

    Optional<CustomerGroup> findById(Long id);

    List<CustomerGroup> findByNameContainingIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<CustomerGroup> findByIsDeletedFalse();
}
