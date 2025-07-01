package com.codewithmanas.skillexplatformbackend.controller;

import com.codewithmanas.skillexplatformbackend.dto.*;
import com.codewithmanas.skillexplatformbackend.exception.InvalidTokenException;
import com.codewithmanas.skillexplatformbackend.service.AuthService;
import com.codewithmanas.skillexplatformbackend.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Register User
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO, HttpServletRequest httpServletRequest) {

        String path = httpServletRequest.getRequestURI(); // Dynamically gets /api/auth/register
        String requestId = UUID.randomUUID().toString(); // generate requestId

        RegisterResponseDTO registerResponseDTO = authService.registerUser(registerRequestDTO);

        log.info("[requestId={}] Received register request", requestId);

        ApiResponse<String> response = new ApiResponse<>(
                201,
                "User registered. Please verify your email.",
                "",
                null,
                requestId,
                path
        );

        return ResponseEntity.status(201).body(response);
    }

    // Verify Email
    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestParam String token, HttpServletRequest httpServletRequest) {

        String path = httpServletRequest.getRequestURI();
        String requestId = UUID.randomUUID().toString();

        if(token.trim().isEmpty()) {
                throw new InvalidTokenException("Token is missing");
        }

        String verifiedMessage = authService.verifyEmail(token);

        log.info("[requestId={}] Received verify-email request", requestId);

        ApiResponse<String> response = new ApiResponse<>(
                200,
                "Email Verified Successfully",
                verifiedMessage,
                null,
                requestId,
                path
        );

        return ResponseEntity.ok().body(response);
    }

    // Login User
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletRequest httpServletRequest) {

        String path = httpServletRequest.getRequestURI();
        String requestId = UUID.randomUUID().toString();

        LoginResponseDTO loginResponseDTO = authService.loginUser(loginRequestDTO);

        log.info("[requestId={}] Received login request", requestId);

        ApiResponse<LoginResponseDTO> response = new ApiResponse<>(
                200,
                "Logged in successful",
                loginResponseDTO,
                null,
                requestId,
                path

        );


        return ResponseEntity.ok().body(response);
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
