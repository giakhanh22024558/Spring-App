package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderRequest {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String nameText;

    private String address;

    private String phone;

    @Email(message = "Email must be valid")
    private String email;

    private LocalDate birthday;

    private String description;

    private Long providerGroupId;
}
