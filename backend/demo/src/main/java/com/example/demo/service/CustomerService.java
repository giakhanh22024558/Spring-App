package com.example.demo.service;

import com.example.demo.dto.CustomerRequest;
import com.example.demo.dto.CustomerResponse;
import com.example.demo.model.customers.Customer;
import com.example.demo.model.customers.CustomerGroup;
import com.example.demo.repository.customers.CustomerGroupRepository;
import com.example.demo.repository.customers.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerGroupRepository customerGroupRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, CustomerGroupRepository customerGroupRepository) {
        this.customerRepository = customerRepository;
        this.customerGroupRepository = customerGroupRepository;
    }

    public Page<CustomerResponse> findAll(String name, Long groupId, Pageable pageable) {
        Page<Customer> customers;

        if (name != null && groupId != null) {
            customers = customerRepository.findByNameContainingIgnoreCaseAndGroupId(name, groupId, pageable);
        } else if (name != null) {
            customers = customerRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if (groupId != null) {
            customers = customerRepository.findByGroupId(groupId, pageable);
        } else {
            customers = customerRepository.findAll(pageable);
        }

        return customers.map(this::convertToResponse);
    }

    public CustomerResponse findById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));
        return convertToResponse(customer);
    }

    @Transactional
    public CustomerResponse create(CustomerRequest customerRequest) {
        Customer customer = convertToEntity(customerRequest);
        customer.setCreatedTime(LocalDateTime.now());
        customer.setUpdatedTime(LocalDateTime.now());
        customer = customerRepository.save(customer);
        return convertToResponse(customer);
    }

    @Transactional
    public CustomerResponse update(Long id, CustomerRequest customerRequest) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));

        updateCustomerFromRequest(customer, customerRequest);
        customer.setUpdatedTime(LocalDateTime.now());
        customer = customerRepository.save(customer);
        return convertToResponse(customer);
    }

    @Transactional
    public void delete(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));

        customer.setIsDeleted(true);
        customer.setUpdatedTime(LocalDateTime.now());
        customerRepository.save(customer);
    }

    public List<CustomerResponse> findByGroupId(Long groupId) {
        List<Customer> customers = customerRepository.findByGroupId(groupId);
        return customers.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private Customer convertToEntity(CustomerRequest customerRequest) {
        Customer customer = new Customer();
        updateCustomerFromRequest(customer, customerRequest);
        return customer;
    }

    private void updateCustomerFromRequest(Customer customer, CustomerRequest request) {
        customer.setName(request.getName());
        customer.setNameText(request.getNameText());
        customer.setAddress(request.getAddress());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        customer.setBirthday(request.getBirthday());
        customer.setBuyDate(request.getBuyDate());
        customer.setDescription(request.getDescription());
        customer.setGender(request.getGender());
        customer.setIsDeleted(request.getIsDeleted() != null ? request.getIsDeleted() : false);

        if (request.getGroupId() != null) {
            CustomerGroup group = customerGroupRepository.findById(request.getGroupId())
                    .orElseThrow(() -> new EntityNotFoundException("Customer Group not found with id: " + request.getGroupId()));
            customer.setGroup(group);
        }
    }

    private CustomerResponse convertToResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setNameText(customer.getNameText());
        response.setAddress(customer.getAddress());
        response.setPhone(customer.getPhone());
        response.setEmail(customer.getEmail());
        response.setBirthday(customer.getBirthday());
        response.setBuyDate(customer.getBuyDate());
        response.setDescription(customer.getDescription());
        response.setGender(customer.getGender());
        response.setCreatedTime(customer.getCreatedTime());
        response.setUpdatedTime(customer.getUpdatedTime());
        response.setIsDeleted(customer.getIsDeleted());

        if (customer.getGroup() != null) {
            response.setGroupId(customer.getGroup().getId());
            response.setGroupName(customer.getGroup().getName());
        }

        return response;
    }
}