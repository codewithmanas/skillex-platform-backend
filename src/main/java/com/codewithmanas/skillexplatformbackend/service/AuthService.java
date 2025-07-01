package com.codewithmanas.skillexplatformbackend.service;

import com.codewithmanas.skillexplatformbackend.dto.LoginRequestDTO;
import com.codewithmanas.skillexplatformbackend.dto.LoginResponseDTO;
import com.codewithmanas.skillexplatformbackend.dto.RegisterRequestDTO;
import com.codewithmanas.skillexplatformbackend.dto.RegisterResponseDTO;
import com.codewithmanas.skillexplatformbackend.entity.User;
import com.codewithmanas.skillexplatformbackend.exception.*;
import com.codewithmanas.skillexplatformbackend.mapper.AuthMapper;
import com.codewithmanas.skillexplatformbackend.repository.UserRepository;
import com.codewithmanas.skillexplatformbackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    // Register User
    public RegisterResponseDTO registerUser(RegisterRequestDTO registerRequestDTO) {

        // Sanitize and Normalize email and password
        String email = registerRequestDTO.getEmail().trim().toLowerCase();

        // Check if email already exists
        if(userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email is already in use.");
        }

        String encodedPassword = passwordEncoder.encode(registerRequestDTO.getPassword());

        User user = userRepository.save(AuthMapper.toEntity(registerRequestDTO, encodedPassword));
        RegisterResponseDTO registerResponseDTO = AuthMapper.toDTO(user);

        // Send email verification
        sendVerification(email, user.getId().toString());

        return registerResponseDTO;
    }

    public void sendVerification(String email, String userId) {
        String token = jwtUtil.generateVerificationToken(email, userId);
        String link = frontendBaseUrl + "/verify-email" + "?token=" + token;
        emailService.sendVerificationEmail(email, link);
    }

    // Verify Email
    public String verifyEmail(String token) {
        if(jwtUtil.isTokenInvalid(token)) {
           throw new InvalidTokenException("Token is expired");
        }

        String userId = jwtUtil.extractUserId(token);

        User user = userRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new InvalidTokenException("User not found"));

        if(user.isVerified()) {
            throw new EmailAlreadyVerifiedException("Email Already Verified");
        }

        user.setVerified(true);
        user.setEmailVerifiedAt(Instant.now());

        userRepository.save(user);

        return "Email verified successfully";
    }


    // Login User
    public LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO) {

        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        // Validate password
        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getHashedPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        // Check if email is verified
        if (!user.isVerified()) {
            throw new EmailNotVerifiedException("Email not verified. Please verify your email before logging in.");
        }

        String refreshToken = jwtUtil.generateRefreshToken(user.getId().toString(), user.getRole().name());
        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getId().toString(), user.getRole().name());


        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setAccessToken(accessToken);
        loginResponseDTO.setRefreshToken(refreshToken);
        loginResponseDTO.setExpiresIn(86400);

//        String refreshToken =  userRepository.findByEmail(loginRequestDTO.getEmail())
//                .filter(user -> passwordEncoder.matches(loginRequestDTO.getPassword(), user.getHashedPassword()))
//                .map(user -> jwtUtil.generateRefreshToken(loginRequestDTO.getEmail(), user.getRole().name()))
//                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
//
//        String accessToken =  userRepository.findByEmail(loginRequestDTO.getEmail())
//                .filter(user -> passwordEncoder.matches(loginRequestDTO.getPassword(), user.getHashedPassword()))
//                .map(user -> jwtUtil.generateAccessToken(loginRequestDTO.getEmail(), user.getId().toString(), user.getRole().name()))
//                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));



        return loginResponseDTO;

    }

    // Forgot Password
    public boolean forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new InvalidCredentialsException("User does not exist!"));

        // send reset password email
        sendResetPasswordEmail(email, user.getId().toString());

        return true;
    }

    public void sendResetPasswordEmail(String email, String id) {
        String token = jwtUtil.generateResetPasswordToken(email, id);
        String link = frontendBaseUrl + "/reset-password" + "?token=" + token;
        emailService.sendResetPasswordEmail(email, link);
    }

    // Reset Password
    public boolean resetPassword(String newPassword, String token) {

        String userId = jwtUtil.extractUserId(token);

        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new InvalidTokenException("Invalid Token"));

        if(jwtUtil.isTokenInvalid(token)) {
            throw new InvalidTokenException("Token is expired");
        }

        // Check if new password is same as old password
        if(passwordEncoder.matches(newPassword, user.getHashedPassword())) {
            throw new SameAsOldPasswordException("New password must be different from the old password");
        }

        String encodedPassword = passwordEncoder.encode(newPassword);

        // Update user password
        user.setHashedPassword(encodedPassword);
        userRepository.save(user);

        return true;
    }

}
