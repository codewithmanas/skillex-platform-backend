package com.codewithmanas.skillexplatformbackend.dto;

import jakarta.validation.constraints.NotBlank;

public class ResetPasswordRequestDTO {

    @NotBlank(message = "Token is not provided")
    private String token;

    @NotBlank(message = "New Password is required")
    private String newPassword;

    public @NotBlank(message = "Token is not provided") String getToken() {
        return token;
    }

    public void setToken(@NotBlank(message = "Token is not provided") String token) {
        this.token = token;
    }

    public @NotBlank(message = "New Password is required") String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(@NotBlank(message = "New Password is required") String newPassword) {
        this.newPassword = newPassword;
    }
}
