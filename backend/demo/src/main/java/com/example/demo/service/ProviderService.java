package com.example.demo.service;

import com.example.demo.dto.ProviderRequest;
import com.example.demo.model.providers.Provider;
import com.example.demo.model.providers.ProviderGroup;
import com.example.demo.repository.providers.ProviderGroupRepository;
import com.example.demo.repository.providers.ProviderRepository;
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
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final ProviderGroupRepository providerGroupRepository;

    @Autowired
    public ProviderService(ProviderRepository providerRepository, ProviderGroupRepository providerGroupRepository) {
        this.providerRepository = providerRepository;
        this.providerGroupRepository = providerGroupRepository;
    }

    public Page<Map<String, Object>> findAll(String name, Long groupId, Pageable pageable) {
        Page<Provider> providers;

        if (name != null && groupId != null) {
            providers = providerRepository.findByNameContainingIgnoreCaseAndProviderGroupId(name, groupId, pageable);
        } else if (name != null) {
            providers = providerRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if (groupId != null) {
            providers = providerRepository.findByProviderGroupId(groupId, pageable);
        } else {
            providers = providerRepository.findAll(pageable);
        }

        return providers.map(this::convertToResponse);
    }

    public Map<String, Object> findById(Long id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Provider not found with id: " + id));
        return convertToResponse(provider);
    }

    @Transactional
    public Map<String, Object> create(ProviderRequest providerRequest) {
        Provider provider = convertToEntity(providerRequest);
        provider.setCreatedTime(LocalDateTime.now());
        provider.setUpdatedTime(LocalDateTime.now());
        provider = providerRepository.save(provider);
        return convertToResponse(provider);
    }

    @Transactional
    public Map<String, Object> update(Long id, ProviderRequest providerRequest) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Provider not found with id: " + id));

        updateProviderFromRequest(provider, providerRequest);
        provider.setUpdatedTime(LocalDateTime.now());
        provider = providerRepository.save(provider);
        return convertToResponse(provider);
    }

    @Transactional
    public void delete(Long id) {
        if (!providerRepository.existsById(id)) {
            throw new EntityNotFoundException("Provider not found with id: " + id);
        }
        providerRepository.deleteById(id);
    }

    public List<Map<String, Object>> findByGroupId(Long groupId) {
        List<Provider> providers = providerRepository.findByProviderGroupId(groupId);
        return providers.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private Provider convertToEntity(ProviderRequest providerRequest) {
        Provider provider = new Provider();
        updateProviderFromRequest(provider, providerRequest);
        return provider;
    }

    private void updateProviderFromRequest(Provider provider, ProviderRequest request) {
        provider.setName(request.getName());
        provider.setNameText(request.getNameText());
        provider.setAddress(request.getAddress());
        provider.setPhone(request.getPhone());
        provider.setEmail(request.getEmail());
        provider.setBirthday(request.getBirthday());
        provider.setDescription(request.getDescription());

        if (request.getProviderGroupId() != null) {
            ProviderGroup group = providerGroupRepository.findById(request.getProviderGroupId())
                    .orElseThrow(() -> new EntityNotFoundException("Provider Group not found with id: " + request.getProviderGroupId()));
            provider.setProviderGroup(group);
        }
    }

    private Map<String, Object> convertToResponse(Provider provider) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", provider.getId());
        response.put("name", provider.getName());
        response.put("nameText", provider.getNameText());
        response.put("address", provider.getAddress());
        response.put("phone", provider.getPhone());
        response.put("email", provider.getEmail());
        response.put("birthday", provider.getBirthday());
        response.put("description", provider.getDescription());
        response.put("createdTime", provider.getCreatedTime());
        response.put("updatedTime", provider.getUpdatedTime());

        if (provider.getProviderGroup() != null) {
            response.put("providerGroupId", provider.getProviderGroup().getId());
            response.put("providerGroupName", provider.getProviderGroup().getName());
        }

        return response;
    }
}
