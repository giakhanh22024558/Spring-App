package com.example.demo.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String password;
    private String email;
    private String role;
    private String firstName;
    private String lastName;
}

