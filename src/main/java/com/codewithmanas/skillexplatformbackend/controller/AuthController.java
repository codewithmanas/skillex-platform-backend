package com.codewithmanas.skillexplatformbackend.controller;

import com.codewithmanas.skillexplatformbackend.dto.LoginRequestDTO;
import com.codewithmanas.skillexplatformbackend.dto.RegisterRequestDTO;
import com.codewithmanas.skillexplatformbackend.dto.RegisterResponseDTO;
import com.codewithmanas.skillexplatformbackend.dto.ResetPasswordRequestDTO;
import com.codewithmanas.skillexplatformbackend.service.AuthService;
import com.codewithmanas.skillexplatformbackend.util.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Register User
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        RegisterResponseDTO authResponseDTO = authService.registerUser(registerRequestDTO);
        ApiResponse<String> response = new ApiResponse<>(
                201,
                "User registered. Please verify your email.",
                "",
                null,
                UUID.randomUUID().toString(),
                "/api/auth/register"
        );

        return ResponseEntity.status(201).body(response);
    }

    // Verify Email
    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {

        String response = authService.verifyEmail(token);

        return ResponseEntity.ok().body(response);
    }

    // Login User
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        boolean loggedIn = authService.loginUser(loginRequestDTO);

        ApiResponse<String> response = new ApiResponse<>(
                200,
                "Logged in successful",
                null,
                null,
                UUID.randomUUID().toString(),
                "/api/auth/login"

        );

        if (loggedIn) {
            return ResponseEntity.status(200).body(response);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    // Get User Details
    @GetMapping("/me")
    public ResponseEntity<String> getUser(@RequestBody UUID id) {
        return ResponseEntity.ok().body("User details fetched successfully");
    }

    // Forgot Password
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        if(!authService.forgotPassword(email)) {
            return ResponseEntity.ok().body("Failed to send reset password email");
        }

        return ResponseEntity.ok().body("Reset password email sent successfully");
    }

    // Reset Password
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO) {

        boolean isSuccess = authService.resetPassword(resetPasswordRequestDTO.getNewPassword(), resetPasswordRequestDTO.getToken());

        if(isSuccess) {
            return ResponseEntity.ok().body("Reset Password Successfully");
        }

        return ResponseEntity.ok().body("Reset Password Failed");

    }

}
