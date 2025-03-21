package com.example.demo.controller;

import com.example.demo.config.KeycloakProperties;
import com.example.demo.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequest request) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setEnabled(true);
        user.setCredentials(Collections.singletonList(createPasswordCredential(request.getPassword())));
        user.setRealmRoles(Collections.singletonList("USER")); // Gán quyền mặc định

        keycloak.realm(keycloakProperties.getRealm()).users().create(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }

    private CredentialRepresentation createPasswordCredential(String password) {
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);
        return passwordCred;
    }
}

