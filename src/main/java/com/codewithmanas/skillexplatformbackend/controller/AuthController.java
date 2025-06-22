package com.codewithmanas.skillexplatformbackend.controller;

import com.codewithmanas.skillexplatformbackend.dto.AuthRequestDTO;
import com.codewithmanas.skillexplatformbackend.dto.AuthResponseDTO;
import com.codewithmanas.skillexplatformbackend.service.AuthService;
import com.codewithmanas.skillexplatformbackend.util.ApiResponse;
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
    public ResponseEntity<ApiResponse<AuthResponseDTO>> register(@RequestBody AuthRequestDTO authRequestDTO) {
        AuthResponseDTO authResponseDTO = authService.registerUser(authRequestDTO);
        ApiResponse<AuthResponseDTO> response = new ApiResponse<>(
                201,
                "User registered successfully",
                authResponseDTO,
                null,
                UUID.randomUUID().toString(),
                "/api/auth/register"
        );

        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequestDTO authRequestDTO) {
        System.out.println("email: " + authRequestDTO.getEmail());
        System.out.println("password: " + authRequestDTO.getPassword());

//        AuthResponseDTO authResponseDTO = authService.loginUser(authRequestDTO);
        boolean loggedIn = authService.loginUser(authRequestDTO);

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
