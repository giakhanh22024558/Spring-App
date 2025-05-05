package com.example.demo.repository.providers;

import com.example.demo.model.providers.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

    Optional<Provider> findById(Long id);

    List<Provider> findByProviderGroupId(Long groupId);

    // Thêm phương thức phân trang cho tìm kiếm theo group id
    Page<Provider> findByProviderGroupId(Long groupId, Pageable pageable);

    List<Provider> findByNameContainingIgnoreCase(String name);

    // Thêm phương thức phân trang cho tìm kiếm theo tên
    Page<Provider> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Thêm phương thức phân trang cho tìm kiếm kết hợp tên và group id
    Page<Provider> findByNameContainingIgnoreCaseAndProviderGroupId(String name, Long groupId, Pageable pageable);

    List<Provider> findByEmail(String email);

    List<Provider> findByBirthday(LocalDate birthday);

    List<Provider> findByAddressContainingIgnoreCase(String address);

    List<Provider> findByPhone(String phone);
}