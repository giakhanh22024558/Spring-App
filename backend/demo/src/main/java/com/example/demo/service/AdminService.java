package com.example.demo.service;

import com.example.demo.dto.UserRequest;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final Keycloak keycloak;
    private final String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    public AdminService(Keycloak keycloak, @Value("${keycloak.realm}") String realm) {
        this.keycloak = keycloak;
        this.realm = realm;
    }

    // 🔹 Get all users
    public List<Map<String, Object>> getAllUsers() {
        try {
            return keycloak.realm(realm).users().list().stream().map(user -> {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("id", user.getId());
                userMap.put("firstName", Optional.ofNullable(user.getFirstName()).orElse(""));
                userMap.put("lastName", Optional.ofNullable(user.getLastName()).orElse(""));
                userMap.put("email", Optional.ofNullable(user.getEmail()).orElse(""));
                userMap.put("username", user.getUsername());
                userMap.put("roles", getUserRoles(user.getId())); // Fetch roles separately
                return userMap;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            return List.of(Map.of("error", "Failed to retrieve users", "message", e.getMessage()));
        }
    }

    // 🔹 Get user roles
    private List<String> getUserRoles(String userId) {
        try {
            var userResource = keycloak.realm(realm).users().get(userId);
            List<String> realmRoles = userResource.roles().realmLevel().listAll()
                    .stream().map(RoleRepresentation::getName).collect(Collectors.toList());
            List<String> clientRoles = userResource.roles().clientLevel(getClientUUID()).listAll()
                    .stream().map(RoleRepresentation::getName).collect(Collectors.toList());
            realmRoles.addAll(clientRoles);
            return realmRoles;
        } catch (Exception e) {
            return List.of("Error fetching roles: " + e.getMessage());
        }
    }

    // 🔹 Get Client UUID
    private String getClientUUID() {
        try {
            return keycloak.realm(realm).clients().findByClientId(clientId).get(0).getId();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve client UUID: " + e.getMessage());
        }
    }

    // 🔹 Create a new user
    public Map<String, Object> createUser(String username, String password, String email, String role) {
        try {
            if (role == null || role.trim().isEmpty()) {
                return Map.of("error", "A user role must be specified.");
            }
            if ("admin".equalsIgnoreCase(role)) {
                return Map.of("error", "Not permitted to create an admin user.");
            }

            UserRepresentation user = new UserRepresentation();
            user.setUsername(username);
            user.setEmail(email);
            user.setEnabled(true);

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            credential.setTemporary(false);
            user.setCredentials(List.of(credential));

            Response response = keycloak.realm(realm).users().create(user);
            if (response.getStatus() == 201) {
                String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                assignUserRole(userId, role);
                return Map.of("message", "User created successfully!", "userId", userId, "role", role);
            } else {
                return Map.of("error", "Failed to create user", "status", response.getStatus());
            }
        } catch (Exception e) {
            return Map.of("error", "Failed to create user", "message", e.getMessage());
        }
    }

    // 🔹 Assign a role to the user
    private void assignUserRole(String userId, String roleName) {
        try {
            var userResource = keycloak.realm(realm).users().get(userId);
            var clientUUID = getClientUUID();
            List<RoleRepresentation> roles = keycloak.realm(realm).clients().get(clientUUID)
                    .roles().list().stream().filter(r -> r.getName().equalsIgnoreCase(roleName))
                    .collect(Collectors.toList());
            if (!roles.isEmpty()) {
                userResource.roles().clientLevel(clientUUID).add(roles);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to assign role: " + e.getMessage());
        }
    }

    // 🔹 Update a user
    public Map<String, Object> updateUser(String userId, UserRequest request) {
        try {
            UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();
            if (user == null) {
                return Map.of("error", "User not found!");
            }

            Optional.ofNullable(request.getFirstName()).ifPresent(user::setFirstName);
            Optional.ofNullable(request.getLastName()).ifPresent(user::setLastName);
            Optional.ofNullable(request.getEmail()).ifPresent(user::setEmail);
            Optional.ofNullable(request.getUsername()).ifPresent(user::setUsername);

            keycloak.realm(realm).users().get(userId).update(user);
            return Map.of("message", "User updated successfully!", "userId", userId);
        } catch (Exception e) {
            return Map.of("error", "Failed to update user", "message", e.getMessage());
        }
    }

    // 🔹 Get user details by ID
    public Map<String, Object> getUserById(String userId) {
        try {
            UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();
            if (user == null) {
                return Map.of("error", "User not found");
            }
            return Map.of(
                    "id", user.getId(),
                    "firstName", Optional.ofNullable(user.getFirstName()).orElse(""),
                    "lastName", Optional.ofNullable(user.getLastName()).orElse(""),
                    "email", Optional.ofNullable(user.getEmail()).orElse(""),
                    "username", user.getUsername(),
                    "roles", getUserRoles(userId)
            );
        } catch (Exception e) {
            return Map.of("error", "Failed to retrieve user", "message", e.getMessage());
        }
    }

    // 🔹 Delete a user
    public Map<String, Object> deleteUser(String userId) {
        try {
            keycloak.realm(realm).users().get(userId).remove();
            return Map.of("message", "User deleted successfully!", "userId", userId);
        } catch (Exception e) {
            return Map.of("error", "Failed to delete user", "message", e.getMessage());
        }
    }
}
