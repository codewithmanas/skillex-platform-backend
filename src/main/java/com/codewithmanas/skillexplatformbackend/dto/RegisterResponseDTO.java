package com.codewithmanas.skillexplatformbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterResponseDTO {
    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    public @Email @NotBlank String getEmail() {
        return email;
    }

    public void setEmail(@Email @NotBlank String email) {
        this.email = email;
    }

}
