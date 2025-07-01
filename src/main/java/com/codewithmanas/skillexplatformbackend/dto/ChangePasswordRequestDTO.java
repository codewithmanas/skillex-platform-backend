package com.codewithmanas.skillexplatformbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordRequestDTO {
    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String newPassword;

    public @NotBlank(message = "Current password is required") String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(@NotBlank(message = "Current password is required") String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public @NotBlank(message = "New password is required") @Size(min = 8, message = "Password must be at least 8 characters") String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(@NotBlank(message = "New password is required") @Size(min = 8, message = "Password must be at least 8 characters") String newPassword) {
        this.newPassword = newPassword;
    }
}
