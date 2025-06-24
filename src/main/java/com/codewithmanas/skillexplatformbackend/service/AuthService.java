package com.codewithmanas.skillexplatformbackend.service;

import com.codewithmanas.skillexplatformbackend.dto.RegisterRequestDTO;
import com.codewithmanas.skillexplatformbackend.dto.RegisterResponseDTO;
import com.codewithmanas.skillexplatformbackend.entity.User;
import com.codewithmanas.skillexplatformbackend.exception.EmailAlreadyExistsException;
import com.codewithmanas.skillexplatformbackend.mapper.AuthMapper;
import com.codewithmanas.skillexplatformbackend.repository.UserRepository;
import com.codewithmanas.skillexplatformbackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    @Value("${app.frontend.verify-url}")
    private String frontendVerifyUrl;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    public RegisterResponseDTO registerUser(RegisterRequestDTO registerRequestDTO) {

        // Sanitize and Normalize email and password
        String email = registerRequestDTO.getEmail().trim().toLowerCase();

        // Check if email already exists
        boolean userExist = userRepository.existsByEmail(registerRequestDTO.getEmail());

        if(userExist) {
            throw new EmailAlreadyExistsException("Email is already in use.");
        }

        String encodedPassword = passwordEncoder.encode(registerRequestDTO.getPassword());

        User user = userRepository.save(AuthMapper.toEntity(registerRequestDTO, encodedPassword));
        RegisterResponseDTO registerResponseDTO = AuthMapper.toDTO(user);

        // Send email verification
        sendVerification(email);

        return registerResponseDTO;
    }

    public void sendVerification(String email) {
        String token = jwtUtil.generateVerificationToken(email);
        String link = frontendVerifyUrl + "?token=" + token;
        emailService.sendVerificationEmail(email, link);
    }

    // Verify Email
    public String verifyEmail(String token) {
        if(jwtUtil.isTokenExpired(token)) {
            return "Token Expired";
        }

        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if(user.isVerified()) return "User already verified";

        user.setVerified(true);
        user.setEmailVerifiedAt(Instant.now());

        userRepository.save(user);

        return "Email verified successfully";
    }


    public boolean loginUser(RegisterRequestDTO registerRequestDTO) {
        boolean userExist = userRepository.existsByEmail(registerRequestDTO.getEmail());
        Optional<User> user =  userRepository.findByEmail(registerRequestDTO.getEmail());

        if(!userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            System.out.println("Email not found.");
        }

        if(user.isPresent()) {
            System.out.println("PRESENT");
        }

        System.out.println("User exist: " + userExist);

        return userExist;
    }

}
