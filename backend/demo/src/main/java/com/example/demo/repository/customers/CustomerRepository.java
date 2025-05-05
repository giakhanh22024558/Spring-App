package com.example.demo.repository.customers;

import com.example.demo.model.customers.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findById(Long id);

    List<Customer> findByGroupId(Long groupId);

    // Thêm phương thức trả về Page để hỗ trợ phân trang
    Page<Customer> findByGroupId(Long groupId, Pageable pageable);

    List<Customer> findByNameContainingIgnoreCase(String name);

    // Thêm phương thức trả về Page để hỗ trợ phân trang
    Page<Customer> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Thêm phương thức trả về Page để hỗ trợ tìm kiếm kết hợp tên và nhóm
    Page<Customer> findByNameContainingIgnoreCaseAndGroupId(String name, Long groupId, Pageable pageable);

    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);
}