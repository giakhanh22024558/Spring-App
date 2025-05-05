package com.example.demo.controller;

import com.example.demo.dto.ProviderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.ProviderService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/providers")
public class ProviderController {

    private final ProviderService providerService;

    @Autowired
    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @GetMapping
    public ResponseEntity<Page<Map<String, Object>>> getAllProviders(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long groupId,
            Pageable pageable) {
        Page<Map<String, Object>> providers = providerService.findAll(name, groupId, pageable);
        return ResponseEntity.ok(providers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProviderById(@PathVariable Long id) {
        Map<String, Object> provider = providerService.findById(id);
        return ResponseEntity.ok(provider);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createProvider(@Valid @RequestBody ProviderRequest providerRequest) {
        Map<String, Object> newProvider = providerService.create(providerRequest);
        return new ResponseEntity<>(newProvider, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProvider(
            @PathVariable Long id,
            @Valid @RequestBody ProviderRequest providerRequest) {
        Map<String, Object> updatedProvider = providerService.update(id, providerRequest);
        return ResponseEntity.ok(updatedProvider);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvider(@PathVariable Long id) {
        providerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/groups/{groupId}")
    public ResponseEntity<List<Map<String, Object>>> getProvidersByGroup(@PathVariable Long groupId) {
        List<Map<String, Object>> providers = providerService.findByGroupId(groupId);
        return ResponseEntity.ok(providers);
    }
}
