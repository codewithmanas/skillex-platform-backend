package com.codewithmanas.skillexplatformbackend.service;

import com.codewithmanas.skillexplatformbackend.dto.ChangePasswordRequestDTO;
import com.codewithmanas.skillexplatformbackend.entity.User;
import com.codewithmanas.skillexplatformbackend.exception.InvalidCredentialsException;
import com.codewithmanas.skillexplatformbackend.exception.InvalidTokenException;
import com.codewithmanas.skillexplatformbackend.exception.ResourceNotFoundException;
import com.codewithmanas.skillexplatformbackend.exception.SameAsOldPasswordException;
import com.codewithmanas.skillexplatformbackend.repository.UserRepository;
import com.codewithmanas.skillexplatformbackend.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public boolean changePassword(ChangePasswordRequestDTO changePasswordRequestDTO, String token) {

        // Check if token is expired
        if (jwtUtil.isTokenInvalid(token)) {
            throw new InvalidTokenException("Token expired");
        }

        // Extract user ID from token
        String userId = jwtUtil.extractUserId(token);

        // Fetch user from DB
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Validate old password
        if (!passwordEncoder.matches(changePasswordRequestDTO.getCurrentPassword(), user.getHashedPassword())) {
            throw new InvalidCredentialsException("Old password is incorrect");
        }

        // Check if new password is same as old
        if (passwordEncoder.matches(changePasswordRequestDTO.getNewPassword(), user.getHashedPassword())) {
            throw new SameAsOldPasswordException("New password must be different from the old password");
        }

        // Encode new password and save
        user.setHashedPassword(passwordEncoder.encode(changePasswordRequestDTO.getNewPassword()));
        userRepository.save(user);

        String email = jwtUtil.extractEmail(token);

        // Send Change Password Confirmation
         sendChangePasswordConfirmation(email, userId);

        return true;

    }

    public void sendChangePasswordConfirmation(String email, String userId) {
        String token = jwtUtil.generateResetPasswordToken(email, userId);
        String link = frontendBaseUrl + "/reset-password" + "?token=" + token;

        emailService.sendChangePasswordConfirmEmail(email, link);
    }

    // DELETE Own Account
    public void deleteUser(String token) {

        // Check if token is expired
        if(jwtUtil.isTokenInvalid(token)) {
            throw new InvalidTokenException("Token expired");
        }

        // Extract userId from token
        String userId = jwtUtil.extractUserId(token);

        // Fetch user from DB
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Delete User from DB
        userRepository.delete(user);

    }

    // For Testing Purpose
    @Transactional
    public void deleteUserByEmail(String email) {
        if(!userRepository.existsByEmail(email)) {
            throw new ResourceNotFoundException("User with this email does not exist");
        }

        userRepository.deleteByEmail(email);
    }

}
