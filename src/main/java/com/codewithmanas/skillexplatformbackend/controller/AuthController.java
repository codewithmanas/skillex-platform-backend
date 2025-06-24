package com.codewithmanas.skillexplatformbackend.controller;

import com.codewithmanas.skillexplatformbackend.dto.RegisterRequestDTO;
import com.codewithmanas.skillexplatformbackend.dto.RegisterResponseDTO;
import com.codewithmanas.skillexplatformbackend.service.AuthService;
import com.codewithmanas.skillexplatformbackend.util.ApiResponse;
import jakarta.validation.Valid;
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

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {

        String response = authService.verifyEmail(token);

        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody RegisterRequestDTO registerRequestDTO) {
        System.out.println("email: " + registerRequestDTO.getEmail());
        System.out.println("password: " + registerRequestDTO.getPassword());

//        RegisterResponseDTO registerResponseDTO = authService.loginUser(registerRequestDTO);
        boolean loggedIn = authService.loginUser(registerRequestDTO);

        if (loggedIn) {
            return ResponseEntity.status(200).body("Logged in successful");
        }

        return ResponseEntity.status(200).body("login failed");
    }


    @GetMapping("/me")
    public ResponseEntity<String> getUser(@RequestBody UUID id) {
        return ResponseEntity.ok().body("User details fetched successfully");
    }

}
