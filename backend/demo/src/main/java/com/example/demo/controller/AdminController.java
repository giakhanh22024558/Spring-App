package com.example.demo.controller;

import com.example.demo.dto.UserRequest;
import com.example.demo.service.AdminService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "keycloak")
@RestController
@RequestMapping("/admin/api")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/greeting")
    public ResponseEntity<Map<String, String>> greeting() {
        return ResponseEntity.ok(Map.of("message", "Hello, this is from admin"));
    }

    // 1️⃣ Get all users with specific fields
    @GetMapping("/users")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(adminService.createUser(
                userRequest.getUsername(),
                userRequest.getPassword(),
                userRequest.getEmail(),
                userRequest.getRole()
        ));
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable String userId, @RequestBody UserRequest request) {
        return ResponseEntity.ok(adminService.updateUser(userId, request));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable String userId) {
        return ResponseEntity.ok(adminService.getUserById(userId));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable String userId) {
        return ResponseEntity.ok(adminService.deleteUser(userId));
    }
}